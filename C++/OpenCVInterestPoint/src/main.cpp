#include <cv.h>
#include <highgui.h>

#include  <iostream.h>
#include <utility>
#include <time.h>
#include <vector>

#include "RobustMatcher.h"
#include "Helper.h"
#include "ImageMatch.h"

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/types.h>
#include <sys/stat.h>

#include <list>

using namespace cv;

int MAX_PATH = 300;

/**
 * Listet Verzeichnisse und Datein auf.
 */
vector<string>* list_directory(const char* path) {
	DIR *dir;
	struct dirent *ent;
	struct stat stat_buf;

	// Copy the path into a local buffer
	char full_path[MAX_PATH];
	int source_length = strlen(path);
	if (source_length > (MAX_PATH - 1))
		return NULL;
	memcpy(full_path, path, source_length);

	// Try to open the directory
	if ((dir = opendir(path)) == NULL)
		return NULL;

	vector<string>* dateien = new vector<string>();

	// Iterate over all subobjects
	while ((ent = readdir(dir)) != NULL) {
		// Create the full path
		int str_length = strlen(ent->d_name) + 1;
		if (str_length + source_length > MAX_PATH)
			continue;
		memcpy(&full_path[source_length], ent->d_name, str_length);

		// stat doesn't work? Go away
		if (stat(full_path, &stat_buf))
			continue;

		// Get the type of the file (d = directory, - = file, ? = something else)
		char type = '?';
		if (S_ISDIR(stat_buf.st_mode))
			type = 'd';
		else if (S_ISREG(stat_buf.st_mode)) {
			type = '-';
			dateien->push_back(full_path);
		}

		// And at least print the information
		//printf("%c %s\n", type, full_path);
	}
	closedir(dir);

	return dateien;
}

void computeKeypoints(const char* file_in, const char* file_out_original,
		const char* file_out_keypoints) {
	std::vector<cv::KeyPoint> keypoints;

	cv::Mat image_original = cv::imread(file_in, 1);
	cv::Mat image_gray = cv::imread(file_in, 0);

	Helper* helper = new Helper();
	cv::Mat out;

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(file_out_original, image_original);
	cv::imwrite(file_out_keypoints, out);
}

void method2() {
	// vector of keypoints
	std::vector<cv::KeyPoint> keypoints;

	cv::Mat image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0919.png", 1);
	cv::Mat image_gray = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0919.png", 0);

	Helper* helper = new Helper();
	cv::Mat out;
	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_1_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_1_keypoints.png",
			out);

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0926.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0926.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_2_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_2_keypoints.png",
			out);

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0937.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0937.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_3_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_3_keypoints.png",
			out);

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0941.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0941.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_4_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_4_keypoints.png",
			out);

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0945.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0945.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_5_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_5_keypoints.png",
			out);

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0950.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0950.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_6_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_6_keypoints.png",
			out);

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0959.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0959.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_7_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_7_keypoints.png",
			out);

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0961.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0961.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_8_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_8_keypoints.png",
			out);

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0966.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0966.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_9_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_9_keypoints.png",
			out);

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0968.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0968.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_10_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_10_keypoints.png",
			out);

	/*******************************************************************************************************************/

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0971.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0971.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_11_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_11_keypoints.png",
			out);

	/*******************************************************************************************************************/

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0982.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0982.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_12_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_12_keypoints.png",
			out);

	/*******************************************************************************************************************/

	image_original = cv::imread(
			"/Users/christian/Pictures/Pergamon/SAM_0990.png", 1);
	image_gray = cv::imread("/Users/christian/Pictures/Pergamon/SAM_0990.png",
			0);

	helper->detectKeypoints(image_gray, keypoints);

	drawKeypoints(image_gray, keypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_13_original.png",
			image_original);
	cv::imwrite(
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_13_keypoints.png",
			out);

	/*******************************************************************************************************************/

	const char* file_in;
	const char* file_out_original;
	const char* file_out_keypoints;

	/*******************************************************************************************************************/

	file_in = "/Users/christian/Pictures/Pergamon/SAM_0994.png";
	file_out_original =
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_14_original.png";
	file_out_keypoints =
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_14_keypoints.png";
	computeKeypoints(file_in, file_out_original, file_out_keypoints);

	/*******************************************************************************************************************/

	/*******************************************************************************************************************/

	file_in = "/Users/christian/Pictures/Pergamon/SAM_0997.png";
	file_out_original =
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_15_original.png";
	file_out_keypoints =
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_15_keypoints.png";
	computeKeypoints(file_in, file_out_original, file_out_keypoints);

	/*******************************************************************************************************************/

	/*******************************************************************************************************************/

	file_in = "/Users/christian/Pictures/Pergamon/SAM_1005.png";
	file_out_original =
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_16_original.png";
	file_out_keypoints =
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_16_keypoints.png";
	computeKeypoints(file_in, file_out_original, file_out_keypoints);

	/*******************************************************************************************************************/

	/*******************************************************************************************************************/

	file_in = "/Users/christian/Pictures/Pergamon/SAM_1008.png";
	file_out_original =
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_17_original.png";
	file_out_keypoints =
			"/Users/christian/Desktop/Master_LaTex/Konzept_Visuell/Bilder/Bild_17_keypoints.png";
	computeKeypoints(file_in, file_out_original, file_out_keypoints);

	/*******************************************************************************************************************/
}

void method1() {
	// vector of keypoints
	std::vector<cv::KeyPoint> refKeypoints;
	std::vector<cv::KeyPoint> keypoints1;
	std::vector<cv::KeyPoint> keypoints2;
	//std::vector<cv::KeyPoint> keypoints3;

	// Construct the SURF feature detector object
	//cv::SurfFeatureDetector surf(5000.0);

	// Referenzbild
	cv::Mat referenz =
			cv::imread(
					"/Users/christian/SecureDropBox/Photos/SURF/IMG_20120228_192758.jpg",
					0); // open in b&w

	// Andere Bilder
	cv::Mat image1 =
			cv::imread(
					"/Users/christian/SecureDropBox/Photos/SURF/IMG_20120228_192836.jpg",
					0);
	//cv::Mat image2 = cv::imread("/Users/christian/Dropbox/Photos/SURF/blume2.jpg");

	Helper* helper = new Helper();
	// Keypoints berechnen
	double t = (double) getTickCount();
	helper->detectKeypoints(referenz, refKeypoints);
	t = ((double) getTickCount() - t) / getTickFrequency();
	cout << "Times passed compute keypoints ref image: " << t << endl;

	cv::Mat out;
	drawKeypoints(referenz, refKeypoints, out, cv::Scalar(0, 255, 255),
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	//cv::imwrite("/Users/christian/Desktop/Master_LaTex/Implementierung/Bilder/Bild_Keypoints.png", out);

	cv::namedWindow("Color Reduces Image");
	cv::imshow("Color Reduces Image", out);
	cv::waitKey(0);

	cout << "anzahl der keypoints: " << refKeypoints.size() << endl;

	// do something ...

	helper->detectKeypoints(image1, keypoints1);
	//surf.detect(image2,keypoints2);

	//	cout << "size keypoints image1: " << keypoints1.size() << endl;
	//	cout << "size keypoints image2: " << keypoints2.size() << endl;
	//cout << "size keypoints image3: " << keypoints3.size() << endl;

	// Construction of the SURF descriptor extractor+
	//cv::SurfDescriptorExtractor surfDesc;
	// Extraction of the SURF descriptors
	cv::Mat refDescriptors;
	cv::Mat descriptors1;
	cv::Mat descriptors2;

	// Berechnung der Discriptoren auf allen Bildern.
	t = (double) getTickCount();

	helper->extractDescriptors(referenz, refKeypoints, refDescriptors);
	t = ((double) getTickCount() - t) / getTickFrequency();
	cout << "Times passed compute descriptir ref image: " << t << endl;

	cout << "Anzahl der descriptoren: " << *refDescriptors.size << endl;

	helper->extractDescriptors(image1, keypoints1, descriptors1);
	//surfDesc.compute(image2, keypoints2, descriptors2);

	// Draw Keypoints
	cv::Mat image_out;
	/*cv::drawKeypoints(referenz,			// original image
	 refKeypoints,					// vector of keypoints
	 image_out,						// the output image
	 cv::Scalar(255,255,255),	// keypoint color
	 cv::DrawMatchesFlags::DEFAULT);	// drawing flag
	 */
	//cv::waitKey(0);
	// Construction of the matcher
	cv::BruteForceMatcher<cv::L2<float> > matcher;

	// Match the two image descriptors
	std::vector<cv::DMatch> matches_image1;
	//std::vector<cv::DMatch> matches_image2;

	// do something ...

	//Helper *helper = new Helper();
	Helper::saveDescriptorsInFile("/Users/christian/keypoints.xml",
			descriptors1);
	cv::Mat descriptors_loaded;
	t = (double) getTickCount();
	Helper::loadDescriptorsFromFile("/Users/christian/keypoints.xml",
			descriptors_loaded);
	t = ((double) getTickCount() - t) / getTickFrequency();
	cout << "Times passed to load descriptors: " << t << endl;

	t = (double) getTickCount();
	// 1. Alle Bilder mit Referenzbild matchen
	matcher.match(refDescriptors, descriptors_loaded, matches_image1);
	//matcher.match(refDescriptors, descriptors2, matches_image2);

	t = ((double) getTickCount() - t) / getTickFrequency();
	cout << "Times passed to COMPUTE matches ref images: " << t << endl;

	Helper::saveKeypointsInFile("/Users/christian/keypoints.xml", keypoints1);
	//helper->saveDescriptorsInFile("/Users/christian/descriptors.xml", descriptors1);

	cout << "keypoints1 size: " << keypoints1.size() << endl;

	std::vector<cv::KeyPoint> keypoints_loaded;
	t = (double) getTickCount();
	Helper::loadKeyPointsFromFile("/Users/christian/keypoints.xml",
			keypoints_loaded);
	t = ((double) getTickCount() - t) / getTickFrequency();
	cout << "Times passed to load keypoints: " << t << endl;

	cout << "keypoints loaded size: " << keypoints_loaded.size() << endl;
	//cv::drawMatches(referenz, refKeypoints, image1, keypoints_loaded, matches_image1, image_out, cv::Scalar(0,255,0), cv::Scalar(0,0,255), new vector<vector<char> >(),  cv::DrawMatchesFlags::DEFAULT);
	cv::drawMatches(referenz, refKeypoints, image1, keypoints_loaded,
			matches_image1, image_out, cv::Scalar(0, 255, 255),
			cv::Scalar(0, 0, 0));
	//cv::imwrite("/Users/christian/Desktop/Master_LaTex/Implementierung/Bilder/bruteforce.png", image_out);
	/*
	 if (matches_image1 < matches_image2) {
	 cout << "matches_image1 kleiner" << endl;
	 } else {
	 cout << "matches_image1 groe§er" << endl;
	 }*/

	//cout << "distance image1 and image2: " << sum_distance(matches) << endl;
	//cv::waitKey(0);*/
	/*
	 cv::Mat readDescriptors_ref, readDescriptors_image1;
	 cout << "size descriptors1: " << *descriptors1.size << endl;
	 t = (double)getTickCount();
	 cv::FileStorage fs("test.xml", FileStorage::WRITE);
	 fs << "descriptors_ref_image" << refDescriptors;
	 fs << "image1" << descriptors1;
	 fs.release();
	 t = ((double)getTickCount() - t)/getTickFrequency();
	 cout << "Times passed to write descritpors into file: " << t << endl;

	 cv::Mat descriptors4;

	 t = (double)getTickCount();
	 cv::FileStorage fsRead("test.xml", FileStorage::READ);
	 fsRead["descriptors_ref_image"] >> readDescriptors_ref;
	 fsRead["image1"] >> readDescriptors_image1;
	 fsRead.release();
	 t = ((double)getTickCount() - t)/getTickFrequency();
	 cout << "Times passed to read descritpors from file: " << t << endl;
	 cout << "imag1e descriptors size: " << *readDescriptors_image1.size << endl;
	 */

	cv::namedWindow("Image");
	cv::imshow("Image", image_out);
	//cv::imwrite("keypoints_plain_gray.jpg",image_out);

	cv::waitKey(0);

	// Robust Matcher

	// prepare the matcher
	RobustMatcher rmatcher;
	/*rmatcher.setConfidenceLevel(0.98);
	 rmatcher.setMinDistanceToEpipolar(1.0);
	 rmatcher.setRatio(0.65f);*/
	//cv::Ptr<cv::FeatureDetector> pfd = new cv::SurfFeatureDetector(5000);
	//rmatcher.setFeatureDetector(pfd);
	// Get the images
	// Referenzbild
	//image1 = cv::imread("/Users/christian/SecureDropBox/Photos/SURF/IMG_20120228_192758.jpg");
	// Testbild 1
	//cv::Mat image2 = cv::imread("/Users/christian/SecureDropBox/Photos/SURF/IMG_20120228_192836.jpg");
	// Testbild 2:
	image1 =
			cv::imread(
					"/Users/christian/SecureDropBox/Photos/SURF/IMG_20120228_192758.jpg",
					0);

	// Testbild 3:
	cv::Mat image2 =
			cv::imread(
					"/Users/christian/SecureDropBox/Photos/SURF/IMG_20120228_192836.jpg",
					0);

	// Match the two images;
	std::vector<cv::DMatch> matches;
	std::vector<cv::KeyPoint> keypoints3, keypoints4;
	cv::Mat fundemental = rmatcher.match(image1, image2, matches, keypoints1,
			keypoints2);

	/*cv::drawKeypoints(,			// original image
	 keypoints2,					// vector of keypoints
	 image_out,						// the output image
	 cv::Scalar(255,255,255),	// keypoint color
	 cv::DrawMatchesFlags::DRAW_RICH_KEYPOINTS);	// drawing flag
	 */

	cv::drawMatches(image1, keypoints1, image2, keypoints2, matches, image_out,
			cv::Scalar(0, 255, 255), cv::Scalar(0, 0, 0));
	/*
	 // Gebe die Summe der Distancen aus:
	 cout << "sumDistance image1 and image 2: " << sum_distance(matches) << endl;

	 fundemental = rmatcher.match(image1, image3, matches, keypoints1, keypoints3);
	 //cv::drawMatches(image1, keypoints1, image3, keypoints3, matches, image_out, cv::Scalar(255,255,255), cv::DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	 cout << "sumDistance image1 and image 3: " << sum_distance(matches) << endl;

	 fundemental = rmatcher.match(image1, image4, matches, keypoints1, keypoints4);
	 //cv::drawMatches(image1, keypoints1, image4, keypoints4, matches, image_out, cv::Scalar(255,255,255), cv::DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	 cout << "sumDistance image1 and image 4: " << sum_distance(matches) << endl;
	 */

	cv::namedWindow("RobustMatches");
	cv::imshow("Color Reduces Image", image_out);
	cv::waitKey(0);

	//cv::imwrite("/Users/christian/Desktop/Master_LaTex/Implementierung/Bilder/robust.png",image_out);

}



void disjunkt_image_test() {
	vector<string>* dateien = list_directory(
			"/Users/christian/Pictures/Referenzbilder/");
	RobustMatcher* rmatcher = new RobustMatcher();

	for (vector<string>::iterator it = dateien->begin(); it != dateien->end();
			it++) {
		cout << *it << endl;

		// Lade das Bild aus der Datei
		cv::Mat image = cv::imread(*it, 0);
		;
		unsigned int most_matches = 0; // speicher die Anzahl der meisten Matches
		string image2_filename; // speichert den Dateinamen von Bild 2 mit den meisten Matches
		string second_best_match_image_name;

		unsigned int second_most_matches = 0;

		std::vector<cv::KeyPoint> keypoints1;
		std::vector<cv::KeyPoint> keypoints2;
		std::vector<cv::DMatch> matches;


		std::list<ImageMatch*>* allImageMatches = new std::list<ImageMatch*>();
		for (vector<string>::iterator it2 = dateien->begin();
				it2 != dateien->end(); it2++) {
			// Lade ebenfalls das Bild
			cv::Mat image2 = cv::imread(*it2, 0);


			// Berechne SchlŸsselpunkte und Deskriptoren auf beiden Bildern und Teste mit dem RM ob das Bild erkannt wurde.
			rmatcher->match(image, image2, matches, keypoints1, keypoints2);

			if (matches.size() > 0) {
				// Sammle alle Matches > 0 in einem Vector
				ImageMatch* imageMatch = new ImageMatch(*it2, matches.size());
				allImageMatches->push_back(imageMatch);
			}

			//cout << *it << " matches " << *it2 << " with " << matches.size() << " points" << endl;
			/*int matches_size = matches.size();
			if (matches_size < most_matches) {
				if (matches_size > second_most_matches) {
					second_most_matches = matches_size;
					second_best_match_image_name = *it2;
				}
			}

			if (matches_size > most_matches) {
				most_matches = matches_size;
				image2_filename = *it2;
			}*/
		}

		// Wenn allImagesMatches nicht leer ist, dann sortiere diese
		allImageMatches->sort(ImageMatch::compareImageMatch);

		// Speicher den dateiname von Bild 1 als Key in einer Map.
		// Der Dateiname von Bild 2 mit den meisten Matches wird als Value in der Map gespeichert.
/*

		cout << "Ergebnis: " << *it << " == " << image2_filename << " matches: " << most_matches << endl;

		double similarity;
		// €hnliChkeit zum zweitbesten Ermitteln
		if (most_matches > 0) {
			similarity = (double) second_most_matches / (double) most_matches;
		} else {
			similarity = 0;
		}
		cout << "€hnlichkeit zum zweitbesten Bild: " << similarity << " Name: " << second_best_match_image_name << endl;
*/
		cout << "sortierte liste mit ImageMatches: " << endl;
		for (std::list<ImageMatch*>::iterator list_it = allImageMatches->begin(); list_it != allImageMatches->end(); list_it++) {
			ImageMatch* imageMatch = *list_it;
			cout << "Matches: " << imageMatch->getMatchesSize() << endl;
		}

		// Nun kann die €hnlichkeit zu den anderen Bildern ermittelt werden.
		// gehe die list mit den matches durch
		double similarity = 1;
		int firstValue = 0;
		for (std::list<ImageMatch*>::iterator list_it = allImageMatches->begin(); list_it != allImageMatches->end(); list_it++) {
			ImageMatch* imageMatch = *list_it;
			if (firstValue == 0) {
				firstValue = imageMatch->getMatchesSize();
			} else {
				int otherValue = imageMatch->getMatchesSize();
				similarity -= (double) otherValue / (double) firstValue;

			}
		}

		cout << "End-€hnlichkeit des besten Matches: " << similarity << endl;
	}
}

int main(int argc, char** argv) {
	//method1();
	disjunkt_image_test();

	return 1;
}
