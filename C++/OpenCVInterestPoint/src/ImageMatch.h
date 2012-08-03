/*
 * ImageMatch.h
 *
 *  Created on: 03.08.2012
 *      Author: christian
 */

#ifndef IMAGEMATCH_H_
#define IMAGEMATCH_H_

#include <string>

class ImageMatch {

private:
	std::string mImageFileName;
	int mMatchesSize;

public:
	/**
	 * Konstruktor.
	 */
	ImageMatch(std::string imageFileName, int);

	std::string getImageFileName();
	int getMatchesSize();

	// comparison, not case sensitive.
	static bool compareImageMatch(ImageMatch* first, ImageMatch* second);
};



#endif /* IMAGEMATCH_H_ */
