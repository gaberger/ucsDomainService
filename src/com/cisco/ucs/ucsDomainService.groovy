/* -*- ucsapi.groovy -*- ------------------------------------------------- *
*
*   Copyright (C) 2010, Cisco Systems; author Gary Berger.
*
*
*    Vers: 1.0.2
*	  Cleaned up code aggregate http requests
* ----------------------------------------------------------------------- */
package com.cisco.ucs

import groovy.xml.MarkupBuilder
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.XML
import static groovyx.net.http.Method.POST




class ucsDomainService {


  String userName
  String passWord
  String _host
  String _session_
  List ucsFaults = []
  List ucsEvents = []
  List ucsPowerEvents = []
  List ucsTempEvents = []
  Boolean Debug

// All DME Classes

  def primaryClasses = [

          Chassis: 'equipmentChassis',
          Blades: 'computeBlade',
          ServerProfiles: 'lsServer',
          ServersIFIo: 'etherServerIntFIo',
          Switches: 'networkElement',
          SwitchCards: 'equipmentSwitchCard',
          SwitchPortsEth: 'etherPIo',
          SwitchPortsFc: 'fcPIo',
          Adapters: 'adaptorExtEthIf',
          SwitchIOCards: 'equipmentIOCard',
          SwitchIFIo: 'etherSwitchIntFIo',
          Power: 'lsPower'
  ]


  def secondaryClasses = [
          PortGroups: 'portGroup',
          TopSystem: 'topSystem',
          TopRoot: 'topRoot',
          NicConfigured: 'etherNicIfConfig',
          Fans: 'equipmentFan',
          FanModules: 'equipmentFanModule',
          //	'Leds'           :  'equipmentLed',
          Psus: 'equipmentPsu',
          Users: 'aaaUser',
          UserRols: 'aaaUserRole',
          Vlans: 'adaptorVlan',
          Vsans: 'adaptorVsan',
          Tasks: 'fsmTask',
          Faults: 'faultInst',
          MemoryArray: 'memoryArray'
  ]


  def powerState = [
          'down',
          'up',
          'cycle-immediate',
          'cycle-wait',
          'hard-reset-immediate',
          'hard-reset-wait',
          'soft-shut-down',
          'cmos-reset-immediate',
          'bmc-reset-immediate',
          'bmc-reset-wait']

// All DME Methods

  private def configResolveDn = {_session, _dn, _hier ->
    if (Debug) { println "DEBUG: Executing configResolveDn"}
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.configResolveDn(dn: _dn, cookie: _session, inHierarchical: _hier)
    def retval_ = makeHttpRequest(writer)
    return retval_

  }
  private def configScope = {_session, _dn, _clazz, _hier ->
    if (Debug) {println "DEBUG: Executing configScope" }
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.configScope(dn: _dn, cookie: _session, inHierarchical: _hier, inClass: _clazz)
    def retval_ = makeHttpRequest(writer)
    return retval_

  }
  private def configResolveClass = {_session, _clazz, _hier ->
    if (Debug) {println "DEBUG: Executing configResolveClass on class: ${_clazz}" }

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.configResolveClass(cookie: _session, classId: _clazz, inHierarchical: _hier)
    def retval_ = makeHttpRequest(writer)

    return retval_
  }


  private def configResolveChildrenByDn = {_session, _clazz, _inDn, _hier ->
    if (Debug) {println "DEBUG: Executing configResolveChildren on class: ${_clazz} for dn: ${inDn}" }

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.configResolveChildren(cookie: _session, classId: _clazz, inDN: _inDn, inHierarchical: _hier)
    def retval_ = makeHttpRequest(writer)

    return retval_
  }

  private def configResolveChildren = {_session, _clazz, _hier ->
    if (Debug) {println "DEBUG: Executing configResolveChildren on class: ${_clazz}" }

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.configResolveChildren(cookie: _session, classId: _clazz, inHierarchical: _hier)
    def retval_ = makeHttpRequest(writer)

    return retval_
  }

  private def reboot = {_session, _dn, _state ->
    if (Debug) {println "DEBUG: Executing reboot on dn: ${_dn} with state: ${_state}" }
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.configConfMos(cookie: _session, inHierarchical: "no") {
      inConfigs() {
        pair(key: "${_dn}/power") {
          lsPower(dn: "${_dn}/power", state: _state, status: "modified")
        }
      }
    }
    def retval_ = makeHttpRequest(writer)
    return retval_
  }

  private def serviceSPCreate = {_session, _dn, _name, _descr ->
    if (Debug) {println "DEBUG: Executing serviceSPCreate on dn: ${_dn} with name: ${_name}" }
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.configConfMos(cookie: _session) {
      inConfigs() {
        pair(key: _dn) {
          lsServer(dn: _dn, name: _name, descr: _descr)
        }
      }
    }
    def retval_ = makeHttpRequest(writer)
    return retval_
  }

  private def serviceSPDestroy = {_session, _dn, _name, _descr ->
    if (Debug) {println "DEBUG: serviceSPDestroy createSP on dn: ${_dn} with name: ${_name}" }
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.configConfMos(cookie: _session) {
      inConfigs() {
        pair(key: _dn) {
          lsServer(dn: _dn, name: _name, status: 'deleted')
        }
      }
    }
    def retval_ = makeHttpRequest(writer)
    return retval_
  }

//  private def getMacAddr = { _ipAddr ->
//
//    def addr = InetAddress.getByName(_ipAddr);
//    def ni = NetworkInterface.getByIpAddress(addr);
//
//
//    if (ni != null) {
//      byte[] mac = ni.getHardwareAddress();
//      if (mac != null) {
//
//        def sb = new StringBuilder(18);
//        for (byte b: mac) {
//          if (sb.length() > 0)
//            sb.append(':');
//          sb.append(String.format("%02x", b)).toUpperCase();
//        }
//      }
//    }
//    return sb.toString()
//  }







  private def makeHttpRequest = {_body ->
    if (Debug) { println "DEBUG: Executing makeHTTPRequest on host: ${_host} with body: ${_body}" }
    def http = new HTTPBuilder(_host)
    http.request(POST, XML) {
      uri.path = '/nuova'
      body = _body


      response.success = {resp, xml_ ->
        if (Debug) { println "OK: ${resp.statusLine}" }
        if (xml_.attributes().get("errorCode") != null) {
          throw new UnsupportedOperationException("Failure: ${xml_.@errorCode}, Descr: ${xml_.@errorDescr}")

        }
        return xml_
      }

      response.failure = {resp, xml_ ->
        println "DEBUG: Response failure: ${resp}"
        println "Failure: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
        throw new UnsupportedOperationException("HTTP Response Failed")
      }
    }

  }

  //public methods

  public ucsLogin() {
    if (Debug) { println "DEBUG: Executing ucsLogin on host: ${_host} with user: ${userName}" }
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.aaaLogin(inName: userName, inPassword: passWord)
    def retval_ = makeHttpRequest(writer)
    //String cookie = retval_.@outCookie // set session cookie
    return retval_.@outCookie
  }


  public ucsLogout(_session) {
    if (Debug) { println "DEBUG: Executing aaaLogout" }
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.aaaLogout(cookie: _session)
    def retval_ = makeHttpRequest(writer)
    return retval_
  }

  public getMac(_host) {
    if (Debug) { println "DEBUG: Executing getMac" }
    def retval = getHostMacAddr(_host)
    return retval
  }


  public getByClass(_session, _clazz, _hier) {
    if (Debug) { println "DEBUG: Executing getByClass" }
    def result_ = configResolveClass(_session, _clazz, _hier)

    return result_.outConfigs.children()

  }

  public getByDn(_session, _dn, _hier) {
    if (Debug) { println "DEBUG: Executing getByDn" }
    def result_ = configResolveDn(_session, _dn, _hier)

    return result_.outConfig.children()

  }

  public getClassChildren(_session, _clazz, _inDn, _hier) {
    if (Debug) { println "DEBUG: Executing getClassChildren" }
    def result_ = configResolveChildren(_session, _clazz, _inDn, _hier)

    return result_.outConfigs.children()

  }

  public ServiceProfileCreate(_session, _dn, _name, _descr) {
    if (Debug) { println "DEBUG: Executing ServiceProfileCreate" }
    def result_ = serviceSPCreate(_session, _dn, _name, _descr)
    return result_
  }


  public rebootBylsPower(_session, _dn, _state) {
    if (Debug) { println "DEBUG: Executing rebootBylsPower on dn: ${_dn} with state ${_state}" }
    def retval_ = reboot(_session, _dn, _state)
    return retval_
  }



  public resolveUcsInterfaces(_session) {

    Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
    String phy = ""

    while (en.hasMoreElements()) {
      NetworkInterface ni = en.nextElement()
      byte[] mac = ni.getHardwareAddress()

      def sb = new StringBuilder(18);
      for (byte b in mac) {
        if (sb.length() > 0)
          sb.append(':');
        sb.append(String.format("%02x", b))
        phy = sb.toString().toUpperCase()
      }


      def retval = findDnByMac(_session, phy)

      def dn = retval.split("/")
      def rootdn = "${dn[0]}/${dn[1]}/${dn[2]}"

      def children = getByDn(_session, rootdn, "false")
      children.each {
        if (Debug) { println it.attributes() }
      }


      return
    }


  }

  public findMacbyDn(_session, dn) {
    def retval = null

    getByClass(_session, "adaptorHostEthIf", "false").each {
      String vnicDn = it.@vnicDn.toString()
      if (vnicDn.size() > 0) {
        def tmp = vnicDn.split("/")
        def rootdn = "${tmp[0]}/${tmp[1]}"

        if (dn == rootdn.toString()) {
     if (Debug) { println "Debug: Found dn match $dn on ${it.@dn} for ${it.@mac}"}
          retval = it.@mac.toString()

        }
      }


    }

    return retval  //should return back dn 

  }



  public findDnByMac(_session, mac) {

    def clazz = getByClass(_session, "adaptorHostEthIf", "false")
    def retval = null
    clazz.each {
      def ucsMac = it.@mac.toString()
      if (mac == ucsMac) {
        if (Debug) { println "Debug: Found mac address match $mac on ${it.@dn}"}
        if (Debug) { println it.attributes()}
        retval = it.@dn.toString()
      }
    }

    return retval  //should return back dn
  }

  /////////////////////////////////

  /// Event Library

  public getFaultInst(_session) {
    if (Debug) { println "DEBUG: Executing getFaultInst" }

    def result_ = configResolveClass(_session, 'faultInst', 'false')
    result_.outConfigs.children().each {
      ucsFaults << new ucsFaultInst(

              Id: it.@id, Lc: it.@lc,
              Ack: it.@ack, Type: it.@type,
              Tags: it.@tags, Code: it.@code,
              Rule: it.@rule, Cause: it.@cause,
              Descr: it.@descr, Occur: it.@occur,
              Created: it.@created, Severity: it.@severity,
              ChangeSet: it.@changeset, ChildAction: it.@childaction,
              OrigSeverity: it.@origseverity, PrevSeverity: it.@prevseverity,
              LastTransition: it.@lasttransition, HighestSeverity: it.@highestseverity)
    }
    return ucsFaults

  }

//  public getEventInst(_session) {
//    println "DEBUG: Executing getEventInst"
//
//      ucsEvents << new ucsEventInst (
//              Id:             it.@id,              Code:             it.@code,
//              User:           it.@user,            State:            it.@state,
//              Cause:          it.@cause,           CreatedAt:        it.@createdat,
//              Description:    it.@description,     AffectedObject:   it.@affectedobject)
//
//    return ucsEvents
//
//    }

  public getTempInst(_session) {
    if (Debug) { println "DEBUG: Executing getTempInst" }

    def result_ = configResolveClass(_session, 'computeMbTempStats', 'false')
    result_.outConfigs.children().each {

      ucsTempEvents << new ucsTempInst(

              Dn: it.@dn,
              Update: it.@update,
              Suspect: it.@suspect,
              Threshold: it.@thresholded,
              Intervals: it.@intervals,
              FmTempSenIo: it.@fmTempSenIo,
              TimeCollected: it.@timeCollected,
              FmTempSenRear: it.@fmTempSenRear,
              FmTempSenIoMax: it.@fmTempSenIoMax,
              FmTempSenIoAvg: it.@fmTempSenIoAvg,
              FmTempSenIoMin: it.@fmTempSenIoMin,
              FmTempSenRearAvg: it.@fmTempSenRearAvg,
              FmTempSenRearMin: it.@fmTempSenRearMin,
              FmTempSenRearMax: it.@fmTempSenRearMax)

    }
    return ucsTempEvents
  }   //getTempInst

  public getPowerInst(_session) {
    if (Debug) { println "DEBUG: Executing getPowerInst" }

    def result_ = configResolveClass(_session, 'computeMbPowerStats', 'false')
    result_.outConfigs.children().each {

      ucsPowerEvents << new ucsPowerInst(

              Dn: it.@dn,
              Intervals: it.@intervals,
              Update: it.@update,
              Threshold: it.@thresholded,
              Suspect: it.@suspect,
              InputCurrent: it.@inputCurrent,
              InputVoltage: it.@inputVoltage,
              TimeCollected: it.@timeCollected,
              ConsumedPower: it.@consumedPower,
              InputCurrentMax: it.@inputCurrentMax,
              InputVoltageMax: it.@inputVoltageMax,
              InputVoltageAvg: it.@inputVoltageAvg,
              InputVoltageMin: it.@inputVoltageMin,
              InputCurrentMin: it.@inputCurrentMin,
              InputCurrentAvg: it.@inputCurrentAvg,
              ConsumedPowerMax: it.@consumedPowerMax,
              ConsumedPowerMin: it.@consumedPowerMin,
              ConsumedPowerAvg: it.@consumedPowerAvg)
    }
    return ucsPowerEvents
  } //getPowerInst


} // Class

// End of Lib

////	//ServiceProfile needs to include IP
////	//Blade needs to associate with sp
