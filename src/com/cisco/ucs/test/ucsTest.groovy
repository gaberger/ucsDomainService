package com.cisco.ucs.test


import com.cisco.ucs.ucsDomainService

//LogManager.getLogger('org.apache.http.wire').level = Level.DEBUG
//LogManager.rootLogger.level = Level.DEBUG
//LogManager.getLogger('java.net.Socket').level = Level.DEBUG


class ucsTest {


  public static void main(String[] args) {

//    LogManager.getLogger('org.apache.http.wire').level = Level.DEBUG
//    LogManager.rootLogger.level = Level.DEBUG
//    BasicConfigurator.configure();


    def ssh_user = "root"
    def ssh_pass = "nbv12345"
    def ucs_user = "admin"
    def ucs_pass = "nbv12345"
    def admin_cmd1 = "killall java"
    def giga_cmd1 = "/root/gigaspaces/gigaspaces-xap-premium-7.1.0-ga/bin/gs-agent.sh gsa.global.gsm 0 gsa.gsc 0&"
    def ssh_cmd2 = "ls"
    def ssh_cmd3 = "ps -ef|grep java"


    def ucs = new ucsDomainService()
    ucs._host = "http://10.29.180.4"
    ucs.userName = ucs_user
    ucs.passWord = ucs_pass
    ucs.debug = true


    def session = ucs.ucsLogin()
    //def lsServer = ucs.getByClass(session, 'computePooledSlot', 'false')

    //  def res = ucs.getByDn(session, 'org-root/compute-pool-default', 'true')

//    res.each {
//      println it.dump()
//    }

    // def children = ucs.getClassChildren(session, 'computePooledSlot', 'true')

    def children =  ucs.getByDn(session, "sys/chassis-1/blade-8", "false")
      children.each{
        println it.attributes()
      }

    def retval = ucs.findMacbyDn(session, "org-root/ls-TME_Westmere_Ch02-Blade1")
      println retval



    

//    def children = ucs.getByClass(session, 'computePooledSlot', 'true')
//    children.each {
//      println """
//      Assigned to: ${it.@assignedToDn}
//      PoolableDn:  ${it.@poolableDn}
//      Chassis id:  ${it.@chassisId}
//      Slot id:     ${it.@slotId}
//      Child action: ${it.@childAction}
//      Dn:           ${it.@dn}
//      Owner:       ${it.@owner}
//      PrevAss:    ${it.@prevAssignedToDn}        ucs
//      """
//
//    }

//
//     children = getClassChildren(session, "adaptorHostEthIf",  "false") {
//       children.each {
//         printlin it.dump()
//       }

    //ucs.ucsLogout(session)


  }// main  

} // class

//LogManager.getLogger('org.apache.http.wire').level = Level.DEBUG
//LogManager.rootLogger.level = Level.DEBUG
//LogManager.getLogger('java.net.Socket').level = Level.DEBUG

//ucs.eventParser(session)
//    def computeBlade
//
//    def computeBlade = ucs.getByClass(session, 'computeBlade', 'true')
//    computeBlade.computeBoard.computeMbPowerStats.each
//    {
//      println "Debug: name: ${it.attributes()}"
//    }

//    def env = ucs.getTempInst(session)
//    println env.getClass()

//
//   def faultInst = ucs.getByClass(session, 'faultInst', 'true')
//    faultInst.each
//    {
//      println "Debug: name: ${it.attributes()}"
//    }






