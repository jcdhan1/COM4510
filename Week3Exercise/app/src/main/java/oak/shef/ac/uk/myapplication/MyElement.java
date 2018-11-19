/*
 * Copyright (c) 2017. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.myapplication;

/**
 * this class represents the data supporting the adapter
 */

class MyElement {
	int image;
	String title;
	String preview;

	public MyElement(int image, String title, String preview) {
		this.image = image;
		this.title = title;
		this.preview = preview;
	}
}
