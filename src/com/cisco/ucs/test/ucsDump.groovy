package com.cisco.ucs.test

import com.cisco.ucs.ucsDomainService

/**
 * Created by IntelliJ IDEA.
 * User: gary
 * Date: May 14, 2010
 * Time: 12:30:14 PM
 * To change this template use File | Settings | File Templates.
 */
class ucsDump {


  public static void main(String[] arg) {

    String ucsHost = "http://10.29.180.4"
    String ucsUser = "admin"
    String ucsPass = "nbv12345"

    def Classes = [

            'equipmentChassis',
            'computeBlade',
            'lsServer',
            'etherServerIntFIo',
            'networkElement',
            'equipmentSwitchCard',
            'etherPIo',
            'fcPIo',
            'adaptorExtEthIf',
            'equipmentIOCard',
            'etherSwitchIntFIo',
            'lsPower',
            'portGroup',
            'topSystem',
            'topRoot',
            'etherNicIfConfig',
            'equipmentFan',
            'equipmentFanModule',
            //'equipmentLed',
            'equipmentPsu',
            'aaaUser',
            'aaaUserRole',
            'adaptorVlan',
            'adaptorVsan',
            'fsmTask',
            //'faultInst',
            'memoryArray',
            'computePooledSlot',
            'adaptorHostEthIf'

    ]



    def ucs = new ucsDomainService(_host: ucsHost, userName: ucsUser, passWord: ucsPass, debug: false)
    def session = ucs.ucsLogin()


    for (def clazz in Classes) {

      def gbc = ucs.getByClass(session, clazz, "true")
      gbc.each {
        println "| ${it.name()} |"
        println it.attributes()

      }


    }
  }
}
