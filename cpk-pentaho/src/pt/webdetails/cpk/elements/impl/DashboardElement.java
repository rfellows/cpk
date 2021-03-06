/*!
* Copyright 2002 - 2013 Webdetails, a Pentaho company.  All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

package pt.webdetails.cpk.elements.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import pt.webdetails.cpf.Util;
import pt.webdetails.cpk.CpkEngine;
import pt.webdetails.cpk.InterPluginBroker;
import pt.webdetails.cpk.elements.Element;
import pt.webdetails.cpk.elements.IElement;


import java.util.HashMap;

import pt.webdetails.cpf.utils.IPluginUtils;

public class DashboardElement extends Element {

  private IPluginUtils pluginUtils;

  public DashboardElement() {
  }


    @Override
    public boolean isRenderable() {
        return true;
    }

    @Override
  public void processRequest( Map<String, Map<String, Object>> bloatedMap ) {
    try {
      // element = (DashboardElement) element;
      callCDE( bloatedMap );
    } catch ( Exception ex ) {
      logger.error( "Error while calling CDE: " + Util.getExceptionDescription( ex ) );
    }
  }

  protected void callCDE( Map<String, Map<String, Object>> bloatedMap )
    throws UnsupportedEncodingException, IOException {


    String path = CpkEngine.getInstance().getEnvironment().getPluginUtils().getPluginRelativeDirectory(this.getLocation(), true);

    //ServletRequest wrapper = (HttpServletRequest) bloatedMap.get( "path" ).get( "httprequest" );
    HttpServletResponse response = (HttpServletResponse) bloatedMap.get( "path" ).get( "httpresponse" );
    OutputStream out = response.getOutputStream();

    //String root = wrapper.getScheme() + "://" + wrapper.getServerName() + ":" + wrapper.getServerPort();

    Map<String, Object> params = new HashMap<String, Object>();
    Map<String, Object> requestParams = bloatedMap.get( "request" );

    path = FilenameUtils.separatorsToUnix( FilenameUtils.normalize( path ) );

    params.put( "solution", "system" );
    params.put( "path", path );
    if ( requestParams.containsKey( "mode" ) && requestParams.get( "mode" ).equals( "preview" ) ) {
      params.put( "file", this.getName() + "_tmp.cdfde" );
    } else {
      params.put( "file", this.getName() + ".wcdf" );
    }
    params.put( "absolute", "false" );
    params.put( "inferScheme", "false" );
    //params.put( "root", root );
    //PluginUtils.copyParametersFromProvider( params, requestParams );
    Iterator<String> it = requestParams.keySet().iterator();
    while ( it.hasNext() ) {
      String name = it.next();
      params.put( name, requestParams.get( name ) );
    }

    if ( requestParams.containsKey( "mode" ) && requestParams.get( "mode" ).equals( "edit" ) ) {
      redirectToCdeEditor( response, params );
      return;
    }
    InterPluginBroker.run( params, response, out );
  }

  private void redirectToCdeEditor( HttpServletResponse response,
                                    Map<String, Object> params ) throws IOException {

    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append( "../pentaho-cdf-dd/edit" );
    if ( params.size() > 0 ) {
      urlBuilder.append( "?" );
    }

    List<String> paramArray = new ArrayList<String>();
    for ( String key : params.keySet() ) {
      Object value = params.get( key );
      if ( value instanceof String ) {
        paramArray.add( key + "=" + URLEncoder.encode( (String) value, "utf-8" ) );
      }
    }

    urlBuilder.append( StringUtils.join( paramArray, "&" ) );

    if ( response == null ) {
      logger.error( "response not found" );
      return;
    }
    try {
      response.sendRedirect( urlBuilder.toString() );
    } catch ( IOException e ) {
      logger.error( "could not redirect", e );
    }

  }


   /*
  @Override
  protected ElementInfo createElementInfo() {
    return new ElementInfo( "text/html" );
  }
  */



}
