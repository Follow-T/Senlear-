package com.spring.boot.community.email.image;

public class ImageResource {
	
    public static final String PLACEHOLDERPREFIX = "#";

   
    private final String id;

    
    private final String placeholder;

   
    private final String imageFilePath;

    public ImageResource(String placeholder,String imageFilePath){
        this.placeholder = placeholder;
        this.imageFilePath = imageFilePath;
       
        this.id = String.valueOf(System.nanoTime());
    }

    public String getId(){
        return id;
    }

    public String getPlaceholder(){
        return placeholder;
    }

    public String getImageFilePath(){
        return imageFilePath;
    }

    @Override
    public String toString(){
        return "ImageResource{" + "id=" + id + ", placeholder='" + placeholder + '\'' + ", imageFilePath='" + imageFilePath + '\'' + '}';
    }

	

}
