package com.cisco.ucs.test

import com.cisco.ucs.ucsDomainService

// class

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






class ucsTestSSH {


  public static void main(String[] args) {

    def ssh_user = "root"
    def ssh_pass = "nbv12345"
    def ucs_user = "admin"
    def ucs_pass = "nbv12345"
    def admin_cmd1 = "killall java"
    def giga_cmd1 = "/root/gigaspaces/gigaspaces-xap-premium-7.1.0-ga/bin/gs-agent.sh gsa.global.gsm 0 gsa.gsc 0&"
    def ssh_cmd2 = "ls"
    def ssh_cmd3 = "ps -ef|grep java"


    def ucs = new ucsDomainService()
    ucs._host_ = "http://10.29.180.4"
    ucs.userName = ucs_user
    ucs.passWord = ucs_pass
    ucs.debug = true


    def session = ucs.ucsLogin()
    def lsServer = ucs.getByClass(session, 'lsServer', 'false')




    lsServer.each {
      println "dn: ${it.@dn.toString()}, operState: ${it.@operState.toString()}"
    }

    try {
      def ant = new AntBuilder()
      ant.sshexec(host: "10.29.180.31",
              username: ssh_user,
              password: ssh_pass,
              command: giga_cmd1,
              outputproperty: "cmdOut")

    }
    catch (e) {
      println e
    }

    //println "return code: ${ant.project.properties.cmdExit}"
    //println "stdout: ${ant.project.properties.cmdOut}"
//         keyfile:"/home/localuser/.ssh/id_dsa",

    //ucs.rebootBylsPower(session, "org-root/ls-TME_Westmere_Ch02-Blade5", "up")

    lsServer = ucs.getByClass(session, 'lsServer', 'false')

    lsServer.each {
      println "dn: ${it.@dn.toString()}, operState: ${it.@operState.toString()}"
    }

    ucs.ucsLogout(session)


  }// main

}