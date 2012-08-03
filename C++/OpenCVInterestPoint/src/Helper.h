/*
 * Helper.h
 *
 *  Created on: 12.07.2012
 *      Author: christian
 */

#ifndef HELPER_H_
#define HELPER_H_

#include <cv.h>
#include <vector>
#include <ios>

class Helper {
private:

	cv::Ptr<cv::FeatureDetector> mDetector;
	cv::Ptr<cv::DescriptorExtractor> mExtractor;

public:
	Helper();

	cv::Ptr<cv::FeatureDetector> getFeatureDetector();
	cv::Ptr<cv::DescriptorExtractor> getDescriptorExtractor();

	// Berechne Schlüsselpunkte auf einem Bild
	void detectKeypoints(const cv::Mat image, std::vector<cv::KeyPoint> &keypoints);

	void detectDiscreteKeypoints(const cv::Mat image, std::vector<cv::KeyPoint> &keypoints, int threshold = 10000, int keypoints_size = 10);

	void extractDescriptors(const cv::Mat image, std::vector<cv::KeyPoint> keypoints, cv::Mat &descriptors);

	static void saveKeypointsInFile(const char* nativePath, const std::vector<cv::KeyPoint> keypoints);

	static void saveDescriptorsInFile(const char* nativePath, cv::Mat descriptors);

	static void loadKeyPointsFromFile(const char* nativePath, std::vector<cv::KeyPoint> &keypoints);

	static void loadDescriptorsFromFile(const char* file, cv::Mat &descriptors);
};



#endif /* HELPER_H_ */
