/*!
* Copyright 2002 - 2014 Webdetails, a Pentaho company.  All rights reserved.
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

package pt.webdetails.cpk.elements.impl.kettleoutputs;


import org.codehaus.jackson.map.ObjectMapper;
import org.pentaho.di.core.row.RowMetaInterface;
import pt.webdetails.cpk.elements.impl.KettleResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JsonKettleOutput extends KettleOutput {

  public JsonKettleOutput( HttpServletResponse response, boolean download ) {
    super( response, download );
  }

  @Override
  public void processResult( KettleResult result ) {
    super.processResult( result );

    // TODO: This is assuming that all rows have the same metadata! This could lead to an error.
    RowMetaInterface rowMeta = result.getRows().size() > 0 ? result.getRows().get( 0 ).getRowMeta() : null;
    Collection<Object[]> rows = new ArrayList<Object[]>();
    for ( KettleResult.Row row : result.getRows() ) {
      rows.add( row.getRow() );
    }

    RowsJson rowsJson = new RowsJson( rows, rowMeta );

    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue( this.getOut(), rowsJson );
    } catch ( IOException ex ) {
      this.logger.error( "IO Error processing Json kettle output.", ex );
    }
  }

}
