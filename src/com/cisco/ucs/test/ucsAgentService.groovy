package com.cisco.ucs.test

import java.util.logging.Level
import java.util.logging.LogManager








class ucsAgentService {


  public static void main(String[] arg) {

//    LogManager.rootLogger.level = Level.OFF
//    BasicConfigurator.configure();



    String ucsHost = "http://10.29.180.4"
    String ucsUser = "admin"
    String ucsPass = "nbv12345"

    // need to know who my domainService agent is
    // need to carry the login information here?

//    def ucs = new ucsDomainService(_host: ucsHost, userName: ucsUser, passWord: ucsPass, debug: true)
//    def session = ucs.ucsLogin()
//
//
//    def valObj = ucs.resolveUcsInterfaces(session)


//    Admin admin = new AdminFactory().createAdmin();
//    // wait till things get discovered (you can also use specific waitFor)
//    ElasticServiceManager esm = admin.getElasticServiceManagers().waitForAtLeastOne();
//    ProcessingUnit pu = esm.deploy( new ElasticDataGridDeployment("ucsService").highlyAvailable((false)))
//            .elasticScaleHandler(
//                    .new ElasticScaleHandlerConfig(GigaSpacesLabElasticScaleHandler.class.getName())
//                            .addProperty("machines", "lab-12,lab-13,lab-36,lab-37") ));

//    def gsa = admin.getGridServiceAgents().waitForAtLeastOne()
//    gsa.startGridServiceAndWait()
    

    //   def machine = admin.getMachines()
    //  println machine.getOperatingSystem()

    // for (Machine machine: admin.getMachines ()) {
    //   System.out.println("Machine [" + machine.getUid() + "], " +
    //           "TotalPhysicalMem [" + machine.getOperatingSystem().getDetails().getTotalPhysicalMemorySizeInGB() + "GB], " +
    //           "FreePhysicalMem [" + machine.getOperatingSystem().getStatistics().getFreePhysicalMemorySizeInGB() + "GB]]");


  }
}