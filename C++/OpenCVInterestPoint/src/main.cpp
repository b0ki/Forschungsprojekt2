#include <cv.h>
#include <highgui.h>

#include  <iostream.h>
#include <utility>
#include <time.h>

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
	// Match the two image descriptors
	std::vector<cv::DMatch> matches;

	std::map<String, cv::Mat>::iterator it;
	for (it = descriptors.begin(); it != descriptors.end(); it++) {
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
	// vector of keypoints
	std::vector<cv::KeyPoint> refKeypoints;
	std::vector<cv::KeyPoint> keypoints1;
	std::vector<cv::KeyPoint> keypoints2;
	std::vector<cv::KeyPoint> keypoints3;
	// Construction of the Fast feature detector object
	//cv::FastFeatureDetector fast(40); // threshold for detection
	// feature point detection

	// Construct the SURF feature detector object
	cv::SurfFeatureDetector surf(500.0); // threshold
	/*cv::SiftFeatureDetector sift(
			0.03,
			10.0);*/

	time_t time_first, time_second;
	time_first = time (NULL);

	cv::Mat referenz = cv::imread("/Users/christian/Dropbox/Photos/SURF/IMG_20120225_144106.jpg"); // open in b&w
	cv::Mat image1 = cv::imread("/Users/christian/Dropbox/Photos/SURF/IMG_20120225_144117.jpg");
	cv::Mat image2 = cv::imread("/Users/christian/Dropbox/Photos/SURF/IMG_20120225_144130.jpg");
	cv::Mat image3 = cv::imread("/Users/christian/Dropbox/Photos/SURF/IMG_20120225_144059.jpg");

	time_second = time (NULL);
	cout << "read images in: " << time_second - time_first << endl;
	time_first = time (NULL);

	// Map mit allem Bildern erstellen mit denen verglichen werden soll
	/*std::map<cv::Mat, String> images;
	//images.insert( std::pair<cv::Mat, String>(image1, "") );
	//images.insert( std::pair<cv::Mat, String>(image2,"Am_Ende_der_Sonne") );
	std::pair<String, String> pasgf;
	pasgf.first = "";
	pasgf.second = "";
	images.insert(pasgf);*/

	//sift.detect(image,keypoints);



	surf.detect(image1,keypoints1);
	surf.detect(image2,keypoints2);
	surf.detect(image3,keypoints3);
	surf.detect(referenz,refKeypoints);

	time_second = time (NULL);

	cout << "calculated keypoints in: " << time_second - time_first << endl;

	time_first = time (NULL);

	//fast.detect(image, keypoints);

	//cv::SiftDescriptorExtractor siftDesc;
	// Construction of the SURF descriptor extractor+
	cv::SurfDescriptorExtractor surfDesc;
	// Extraction of the SURF descriptors
	cv::Mat descriptors1;
	cv::Mat descriptors2;
	cv::Mat descriptors3;
	cv::Mat refDescriptors;



	//siftDesc.compute(image, keypoints, descriptors1);

	// 1. Berechnung der Discriptoren auf allen Bildern.
	surfDesc.compute(image1, keypoints1, descriptors1);
	surfDesc.compute(image2, keypoints2, descriptors2);
	surfDesc.compute(image3, keypoints3, descriptors3);
	surfDesc.compute(referenz, refKeypoints, refDescriptors);

	time_second = time (NULL);
	cout << "calculated descriptors in: " << time_second - time_first << endl;
	time_first = time (NULL);

	// 2. Map mit allen Discriptoren berechnen
	std::map<String, cv::Mat> descriptors;
	descriptors.insert(std::pair<String, cv::Mat>("appeal_to_reason", descriptors1));
	descriptors.insert(std::pair<String, cv::Mat>("Am_Ende_der_Sonne", descriptors2));
	descriptors.insert(std::pair<String, cv::Mat>("kathedrale", descriptors3));

	// 3. Map mit allen Matches berechnen
	std::map<String, std::vector<cv::DMatch> > all_matches = match(descriptors, refDescriptors);

	time_second = time (NULL);
	cout << "calculated matches in: " << time_second - time_first << endl;
	time_first = time (NULL);

	// 4. Summe der Distanzen ausrechnen
	std::map<String, float> all_distances = distance(all_matches);

	time_second = time (NULL);
	cout << "calculated distance in: " << time_second - time_first << endl;

	std::map<String, float>::iterator it;
	for (it = all_distances.begin(); it != all_distances.end(); it++) {
		cout << "Image: " << it->first << " distance: " << it->second << endl;
	}

	//cout << "Elements in keypoint vector: " << keypoints1.size() << endl;
	//cout << "Rows in descriptor: " << descriptors1.rows << endl;

	/*cv::drawKeypoints(image,			// original image
			keypoints,					// vector of keypoints
			image,						// the output image
			cv::Scalar(255,255,255),	// keypoint color
			cv::DrawMatchesFlags::DRAW_RICH_KEYPOINTS);	// drawing flag
	*/


/*

	// 1. Alle Bilder mit Referenzbild matchen
	//matcher.match(descriptors1, descriptors1, matches1);
	//matcher.match(descriptors1, descriptors2, matches2);

	// 2. Alle matches in eine Map packen.
	std::map<std::vector<cv::DMatch>, float> m;
	m.insert(std::pair <std::vector<cv::DMatch>, float>(matches1,0));
	m.insert(std::pair <std::vector<cv::DMatch>, float>(matches2,0));

	// 3. Zu jedem Vektor in der Map euklidische Distanz berecehnen,
	euclid_distance(m);



*/
	std::nth_element(all_matches.find("appeal_to_reason")->second.begin(),	// initial position
			all_matches.find("appeal_to_reason")->second.begin()+49,			// position of the sorted element
			all_matches.find("appeal_to_reason")->second.end());				// end position

	// remove all elements after the 25th
	all_matches.find("appeal_to_reason")->second.erase(all_matches.find("appeal_to_reason")->second.begin()+50, all_matches.find("appeal_to_reason")->second.end());

	cv::Mat imageMatches;
	cv::drawMatches(
			referenz,refKeypoints, 	// 1st image and its keypoints
			image1,keypoints1,		// 2nd image and its keypoints
			all_matches.find("appeal_to_reason")->second,
			imageMatches,
			cv::Scalar(255,255,255));	// color of the lines

	cv::namedWindow("Color Reduces Image");
	cv::imshow("Color Reduces Image", imageMatches);


	cv::waitKey(0);

  return 0;
}
