/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package init;

public class InitEnvironment {
	/**
	 * Initialize url(for prod, stage or dev site) based on which branch triggered the build
	 * 
	 * @return String url(for prod, stage or dev site)
	 */
	public static String getUrl(){
		String branch = "not circleci env";
		String url;
		if(System.getenv("CIRCLE_BRANCH")!=null){
			branch = System.getenv("CIRCLE_BRANCH");
		}
		if(branch.equals("prod")){
			url = "https://meetingpackage.com/";
		}
//		else if(branch.equals("stage")){
//			url = "http://mp:blahblahblah@stage.meetingpackage.com/";
//		}
//		else if(branch.equals("develop")){
//			url = "http://mp:blahblahblah@dev.meetingpackage.com/";
//		}
		else if(branch.equals("citest")){
			System.out.println("********;;;;;;;;;:::::::::::!!!!!!!!!");
			url = "https://meetingpackage.com/";
			//url = "http://mp:blahblahblah@stage.meetingpackage.com/";
			//url = "http://mp:blahblahblah@dev.meetingpackage.com/";
		}
		else if(branch.equals("not circleci env")){
			url = "https://meetingpackage.com/";
		}
		else{
			System.out.println(branch);
			url = "https://meetingpackage.com/";
			System.exit(0);
		}
		return url;
	}
	
	/**
	 * 
	 * @return String branch that triggered build
	 */
	public static String getBranch(){
		String branch = "not circleci env";
		if(System.getenv("CIRCLE_BRANCH")!=null){
			branch = System.getenv("CIRCLE_BRANCH");
		}
		return branch;
	}
}
