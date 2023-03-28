package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/unregister")
public class UnregisterController extends AbstractController{

	
	
	public UnregisterController(){
		
		super();
	}
	
	
	//list hte list of cabbages tha the system manages
	
	
	

	
	
	
	
	
	
	
}

--------------------

package com.acme.controller;

import com.acme.model.Product;
import com.acme.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * For ALL AppDirect users
 */
@RestController
@RequestMapping("api")
@Secured({"ROLE_FREE","ROLE_PREMIUM"})
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @RequestMapping("products")
    public Iterable<Product> products() {
        return productRepository.findAll();
    }
}

--------------------

package edu.cornell.library.lambda.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.cornell.library.lambda.model.Language;

@RestController
public class LanguageResource {

    @RequestMapping(path = "/languages", method = RequestMethod.GET)
    public List<Language> listLambdaLanguages() {
        return Arrays.asList(
                new Language("node"), 
                new Language("java"), 
                new Language("python"),
                new Language("ruby")
                );
    }

}

--------------------

