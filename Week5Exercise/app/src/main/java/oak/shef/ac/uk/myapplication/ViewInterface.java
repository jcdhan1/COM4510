/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package oak.shef.ac.uk.myapplication;

public interface ViewInterface {
    public void titleDescritpionInsertedFeedback(String title, String description);
    public void titleDescritpionError(String title, String description, String errorString);
}
