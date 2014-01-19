/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2013] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.cli;

import com.beust.jcommander.*;
import com.beust.jcommander.converters.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * codenvy remote proj:create command and parameters.
 *
 */ 
@Parameters(separators = " ", commandDescription = "Creates a new project in a remote Codenvy cloud workspace")
public class CommandRemoteProjectCreate implements CommandInterface {

    @Parameter(names = { "-h", "--help" }, description = "Prints this help")
	private boolean help;
    public boolean getHelp() { return help; }

    @Parameter(names = "--launch", description = "If set, will launch a browser session and open the new project")
    private boolean launch = false;

    @Parameter(names = "--temp", description = "If set, will create the project in a new, temporary workspace")
    private boolean temp = false;
    
    @ParametersDelegate
	private JSONFileParameterDelegate json_delegate = new JSONFileParameterDelegate();

    @ParametersDelegate
	private CLIAuthParameterDelegate delegate = new CLIAuthParameterDelegate();

    public String getUsageLongDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("Generates a Codenvy Project.  A Codenvy Project is a set of files bound to an IDE,\n");
		sb.append("builder, and runner/debugger with a set of access control lists.  The project will\n");
		sb.append("be created in your remote, named workspace.  You will need to have a valid token\n");
		sb.append("to create the project in your workspace.  If --temp is specified then the project\n");
		sb.append("will be created a new temporary workspace.  If you do not provide a named token,\n");
		sb.append("then the temporary workspace will be accessed anonymously.  Temporary workspaces\n");
		sb.append("will be destroyed if they are idle for IDLE_TIMEOUT, which is set by cloud admins.\n");
		sb.append("By default, Codenvy.com has an IDLE_TIMEOUT of 10 minutes.\n");
		sb.append("\n");
		sb.append("The project will be configured based upon JSON configuration parameters.\n");
		sb.append("Precedence of JSON object loading is --param command line and then --in file.\n");
		sb.append("If --out is specified, the JSON objects will be written to the specified file.\n");
		sb.append("\n");
		sb.append("If '--launch' then a Browser is opened and the new project is opened.  You can\n");
		sb.append("also use 'codenvy remote proj:open' to achieve the same affect.\n");
		sb.append("\n");

		return sb.toString();
	}


	// Create a new project with a set of parameters.
	// 1) Load (override) any parameters in file specified by --in
	// 2) Load (override) any parameters passed in by --param
	// 3a) If !temp, then create the project in your named account
	// 3b) If temp, then create the project in a temporary workspace
	// 4) If --out, then take the resulting URL and output parameters to specified file
	// 5) If --launch, then open a browser session with the new URL
    public void execute() {

    	// Internal JSON Object to store / override parameters.
    	JSONObject factory_params = new JSONObject();
    	String project_url = null;

    	/*
    	 * STEP 1-2: Load any JSON parameters from default.c5y, file specified by --in, or parameters.
    	 */
    	factory_params = JSONFileHelper.loadJSONFromFileAndParams(json_delegate);

    	
    	/* 
    	 * STEP 3: Generate a valid Factory URL.
    	 *         If not encoded, then build manually.
    	 *         If --encoded, then call REST API to generate URL.
    	 */ 
    	if (!temp) {

	    } else {

/*   		
    		CLICredentials cred = CommandAuth.getCredentials(delegate.getProfile(),
                                                             delegate.getProvider(),
                                                             delegate.getUser(),
                                                             delegate.getPassword(),
                                                             delegate.getToken());

		    JSONObject api_return_data = null;
	        JSONObject api_input_data = factory_params;

    		// Format the appropriate input data for this REST command.
    		// Pass the input data into helper command, invoke REST command, and get response.
    		// Parse the response appropriately.
	        api_return_data = RESTAPIHelper.callRESTAPIAndRetrieveResponse(cred, api_input_data, RESTAPIHelper.REST_API_FACTORY_MULTI_PART);

    		if (api_return_data == null)
        		factory_url.append("");
    		else {
        		JSONArray list_of_urls = (JSONArray) api_return_data.get("links");
				Iterator<JSONObject> it = list_of_urls.iterator();
			
				while (it.hasNext()) {

					JSONObject link = (JSONObject) it.next();

					if (link.get("rel").equals("create-project")) {
						factory_url.append(link.get("href"));
					}

				}
    		}
*/
	    }
		

		/* 
    	 * STEP 4: Generate an output file with the contents if outputFile is set.
    	 *         Always output the Factory URL to the console.
    	 */ 
   		JSONFileHelper.writeJSONFile(json_delegate.getOutputFile(), factory_params);
	    

		/* 
    	 * STEP 5: If launch is set, call the native browser with new URL
    	 */ 
    	if (launch) {
	        BrowserHelper.launchNativeBrowserSession(project_url.toString());
		}

	    System.out.println(project_url.toString());

    }
}


