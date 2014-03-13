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

package pt.webdetails.cpk.elements.impl.kettleOutputs;


import javax.servlet.http.HttpServletResponse;

public class SingleCellKettleOutput extends KettleOutput {

  public SingleCellKettleOutput( HttpServletResponse response, boolean download ) {
    super( response, download );
  }

  @Override
  public boolean needsRowListener() {
    return true;
  }

  @Override
  public void processResult() {
    super.processSingleCell();
  }
}
