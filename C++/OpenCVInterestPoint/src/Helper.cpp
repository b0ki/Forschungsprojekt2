/*
 * Helper.cpp
 *
 *  Created on: 12.07.2012
 *      Author: christian
 */



#include "Helper.h"
#include <ostream>

Helper::Helper() {
	//mDetector = cv::FeatureDetector::create("SURF");
	mDetector = new cv::SurfFeatureDetector(4000);
	mExtractor = cv::DescriptorExtractor::create("SURF");
}

cv::Ptr<cv::FeatureDetector> Helper::getFeatureDetector() {
	return mDetector;
}

cv::Ptr<cv::DescriptorExtractor> Helper::getDescriptorExtractor() {
	return mExtractor;
}

void Helper::detectKeypoints(const cv::Mat image, std::vector<cv::KeyPoint> &keypoints) {
	mDetector->detect(image, keypoints);
}

void Helper::detectDiscreteKeypoints(const cv::Mat image, std::vector<cv::KeyPoint> &keypoints, int threshold, int keypoints_size) {
	// Wenn keine Keypoints ermittelt worden sind, dann gehe iterativ mit dem Threshold herunter.
	do {
		threshold -= 100;
		mDetector = new cv::SurfFeatureDetector(threshold);
		mDetector->detect(image, keypoints);
	} while (keypoints.size() < keypoints_size);
}

void Helper::extractDescriptors(const cv::Mat image, std::vector<cv::KeyPoint> keypoints, cv::Mat &descriptors) {
	mExtractor->compute(image, keypoints, descriptors);
}

void Helper::saveKeypointsInFile(const char* nativePath, const std::vector<cv::KeyPoint> keypoints) {
	cv::FileStorage fs(nativePath, cv::FileStorage::WRITE);
	cv::write(fs, "keypoints", keypoints);
	fs.release();
}

void Helper::saveDescriptorsInFile(const char* nativePath, cv::Mat descriptors) {
	cv::FileStorage fs(nativePath, cv::FileStorage::WRITE);
	fs << "descriptors" << descriptors;
	fs.release();
}

void Helper::loadKeyPointsFromFile(const char* nativePath, std::vector<cv::KeyPoint> &keypoints) {
	cv::FileStorage fs(nativePath, cv::FileStorage::READ);
	const cv::FileNode keypointsNode = fs["keypoints"];
	cv::read(keypointsNode ,keypoints);
	fs.release();
}

void Helper::loadDescriptorsFromFile(const char* file, cv::Mat &descriptors) {
	cv::FileStorage fs(file, cv::FileStorage::READ);
	fs["descriptors"] >> descriptors;
	fs.release();
}


