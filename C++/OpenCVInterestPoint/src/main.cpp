#include <cv.h>
#include <highgui.h>

#include  <iostream.h>
#include <utility>
#include <time.h>
#include <vector>
#include "RobustMatcher.h"

using namespace cv;

void sum(cv::DMatch m) {
	cout << m.distance << endl;
}

void print(float f) {
	cout << f << endl;
}

float betrag(std::vector<cv::DMatch> v) {
	float betrag = 4;
	vector<cv::DMatch>::const_iterator i;
	for(i = v.begin(); i != v.end(); i++) {
		betrag += i->distance * i->distance;
	}

	betrag = sqrt(betrag);

	cout << "betrag: " << betrag << endl;
	return betrag;
}

std::vector<float> normVec(std::vector<cv::DMatch> v) {
	float b = betrag(v);

	vector<float> norm;

	vector<cv::DMatch>::const_iterator i;
	for(i = v.begin(); i != v.end(); i++) {
		norm.push_back(i->distance / b);
	}

	return norm;
}
/*
std::vector<float> normVec(std::vector<cv::DMatch> v, float betrag) {
	vector<float> norm;

	vector<cv::DMatch>::const_iterator i;
	for(i = v.begin(); i != v.end(); i++) {
		norm.push_back(i->distance / betrag);
	}

	return norm;
}
*/


/*std::pair <float,std::vector<cv::DMatch> > distance(std::vector<cv::DMatch> reference, std::vector<cv::DMatch> compare_vectors) {
	float distance = 0;
	vector<cv::DMatch>::const_iterator cii;
	for(cii=reference.begin(); cii != reference.end(); cii++) {
		float v1 = cii->distance;
		float v2 = compare_vectors[1].distance;
		euclid_distance += (v1 - v2)*(v1 - v2);
	}

	euclid_distance = sqrt(euclid_distance);
	//cout << "sum: " << euclid_distance << endl;

	std::pair <std::vector<cv::DMatch>, float> p (compare_vectors, euclid_distance);

	return p;
}*/

std::map<String, std::vector<cv::DMatch> > match(std::map<String, cv::Mat> descriptors, cv::Mat reference) {
	std::map<String, std::vector<cv::DMatch> > all_matches;

	// Construction of the matcher
	cv::BruteForceMatcher<cv::L2<float> > matcher;
	//cv::FlannBasedMatcher flann;
	// Match the two image descriptors
	std::vector<cv::DMatch> matches;

	std::map<String, cv::Mat>::iterator it;
	for (it = descriptors.begin(); it != descriptors.end(); it++) {
		//flann.match(reference, it->second, matches);
		matcher.match(reference, it->second, matches);
		all_matches.insert(std::pair<String, std::vector<cv::DMatch> >(it->first, matches));
		matches.clear();
	}

	return all_matches;
}

float sum_distance(std::vector<cv::DMatch> matches) {
	float sum = 0;
	std::vector<cv::DMatch>::iterator it;
	for(it = matches.begin(); it != matches.end(); it++) {
		sum += it->distance;
	}

	return sum;
}

/**
 * Gehe durch alle Matches in er Map.
 * Berechne zu jedem Match die euklidische Distanz zum referenz match.
 */
std::map<String, float> distance(std::map<String, std::vector<cv::DMatch> > all_matches) {
	std::map<String, float> distances;

	std::map<String, std::vector<cv::DMatch> >::iterator it;
	for(it = all_matches.begin(); it != all_matches.end(); it++) {
		distances.insert(std::pair<String, float>(it->first,sum_distance(it->second)));
	}

	return distances;
}

int main( int argc, char** argv ) {
	/*// vector of keypoints
	std::vector<cv::KeyPoint> refKeypoints;
	std::vector<cv::KeyPoint> keypoints1;
	std::vector<cv::KeyPoint> keypoints2;
	//std::vector<cv::KeyPoint> keypoints3;

	// Construct the SURF feature detector object
	cv::SurfFeatureDetector surf(5000.0);

	// Referenzbild
	cv::Mat referenz = cv::imread("/Users/christian/Dropbox/Photos/SURF/blume1.jpg"); // open in b&w

	// Andere Bilder
	cv::Mat image1 = cv::imread("/Users/christian/Dropbox/Photos/SURF/blume1.3.jpg");
	cv::Mat image2 = cv::imread("/Users/christian/Dropbox/Photos/SURF/blume2.jpg");

	// Keypoints berechnen
	surf.detect(referenz,refKeypoints);

	double t = (double)getTickCount();
	// do something ...

	surf.detect(referenz,refKeypoints);
	surf.detect(image1,keypoints1);
	surf.detect(image2,keypoints2);

//	cout << "size keypoints image1: " << keypoints1.size() << endl;
//	cout << "size keypoints image2: " << keypoints2.size() << endl;
	//cout << "size keypoints image3: " << keypoints3.size() << endl;

	// Construction of the SURF descriptor extractor+
	cv::SurfDescriptorExtractor surfDesc;
	// Extraction of the SURF descriptors
	cv::Mat refDescriptors;
	cv::Mat descriptors1;
	cv::Mat descriptors2;

	// Berechnung der Discriptoren auf allen Bildern.
	surfDesc.compute(referenz, refKeypoints, refDescriptors);
	surfDesc.compute(image1, keypoints1, descriptors1);
	surfDesc.compute(image2, keypoints2, descriptors2);

	t = ((double)getTickCount() - t)/getTickFrequency();
	cout << "Times passed compute keypoints and descriptors (in seconds): " << t << endl;

	// Draw Keypoints
	cv::Mat image_out;
	cv::drawKeypoints(image2,			// original image
			keypoints2,					// vector of keypoints
			image_out,						// the output image
			cv::Scalar(255,255,255),	// keypoint color
			cv::DrawMatchesFlags::DRAW_RICH_KEYPOINTS);	// drawing flag


	// Construction of the matcher
	cv::BruteForceMatcher<cv::L2<float> > matcher;

	// Match the two image descriptors
	std::vector<cv::DMatch> matches_image1;
	std::vector<cv::DMatch> matches_image2;

	t = (double)getTickCount();
	// do something ...

	// 1. Alle Bilder mit Referenzbild matchen
	matcher.match(refDescriptors, descriptors1, matches_image1);
	matcher.match(refDescriptors, descriptors2, matches_image2);

	t = ((double)getTickCount() - t)/getTickFrequency();
	cout << "Times passed to match images (in seconds): " << t << endl;

	cout << "distance ref - image1: " << sum_distance(matches_image1) << endl;
	cout << "distance ref - image2: " << sum_distance(matches_image2) << endl;

	if (matches_image1 < matches_image2) {
		cout << "matches_image1 kleiner" << endl;
	} else {
		cout << "matches_image1 groe§er" << endl;
	}

	//cout << "distance image1 and image2: " << sum_distance(matches) << endl;

	//cv::waitKey(0);*/

/*
		FileStorage fs("Features.xml", FileStorage::WRITE);
		fs << "descriptors" << descriptors1;
		fs.release();

		cv::Mat descriptors4;

		FileStorage fsRead("myFile.yml", FileStorage::READ);
		fsRead["descriptors"] >> descriptors4;
		fsRead.release();
*/
/*
		cv::namedWindow("Color Reduces Image");
		cv::imshow("Color Reduces Image", image_out);


		cv::waitKey(0);*/

	// prepare the matcher
		RobustMatcher rmatcher;
		rmatcher.setConfidenceLevel(0.98);
		rmatcher.setMinDistanceToEpipolar(1.0);
		rmatcher.setRatio(0.65f);
		cv::Ptr<cv::FeatureDetector> pfd = new cv::SurfFeatureDetector(10);
		rmatcher.setFeatureDetector(pfd);

		// Get the images
		cv::Mat image1 = cv::imread("/Users/christian/Dropbox/Photos/SURF/IMG_20120228_192758.jpg");
		cv::Mat image2 = cv::imread("/Users/christian/Dropbox/Photos/SURF/IMG_20120228_192836.jpg");

		// Match the two images;
		std::vector<cv::DMatch> matches;
		std::vector<cv::KeyPoint> keypoints1, keypoints2;
		cv::Mat fundemental = rmatcher.match(image1, image2, matches, keypoints1, keypoints2);

		cv::Mat image_out;
		/*cv::drawKeypoints(,			// original image
					keypoints2,					// vector of keypoints
					image_out,						// the output image
					cv::Scalar(255,255,255),	// keypoint color
					cv::DrawMatchesFlags::DRAW_RICH_KEYPOINTS);	// drawing flag
		*/
		cv::drawMatches(image1, keypoints1, image2, keypoints2, matches, image_out, cv::Scalar(255,255,255), cv::DrawMatchesFlags::DRAW_RICH_KEYPOINTS);

		cv::namedWindow("Color Reduces Image");
		cv::imshow("Color Reduces Image", image_out);
		cv::waitKey(0);


  return 0;
}
