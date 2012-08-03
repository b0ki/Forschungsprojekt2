/*
 * ImageMatch.cpp
 *
 *  Created on: 03.08.2012
 *      Author: christian
 */

#include "ImageMatch.h"

ImageMatch::ImageMatch(std::string imageFileName, int matchesSize) {
	mImageFileName = imageFileName;
	mMatchesSize = matchesSize;
}

std::string ImageMatch::getImageFileName() {
	return mImageFileName;
}

int ImageMatch::getMatchesSize() {
	return mMatchesSize;
}

// comparison, not case sensitive.
bool ImageMatch::compareImageMatch (ImageMatch* first, ImageMatch* second) {
	if (first->getMatchesSize() > second->getMatchesSize()) {
		return true;
	}

	return false;
}
