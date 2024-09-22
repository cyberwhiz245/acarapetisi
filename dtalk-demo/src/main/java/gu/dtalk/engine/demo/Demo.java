package gu.dtalk.engine.demo;

import static gu.dtalk.CommonConstant.REDIS_HOST;
import static gu.dtalk.CommonConstant.REDIS_PASSWORD;
import static gu.dtalk.CommonConstant.REDIS_PORT;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import gu.dtalk.engine.SampleConnector;
import gu.simplemq.Channel;
import gu.simplemq.redis.JedisPoolLazy;
import gu.simplemq.redis.RedisFactory;
import gu.simplemq.redis.JedisPoolLazy.PropName;
import net.gdface.utils.NetworkUtil;
import gu.simplemq.redis.RedisConsumer;

import static gu.dtalk.CommonUtils.*;
import static gu.dtalk.engine.SampleConnector.*;

public class Demo {
	/** redis 连接参数 */
	final static Map<PropName, Object> redisParam = 
			ImmutableMap.<PropName, Object>of(
					/** redis 主机名 */PropName.host,REDIS_HOST,
					/** redis 端口号 */PropName.port,REDIS_PORT,
					/** redis 密码    */PropName.password,REDIS_PASSWORD
					);
	private final SampleConnector connAdapter;
	private final RedisConsumer consumer;
	private final byte[] devMac;
	public Demo() {
		JedisPoolLazy pool = JedisPoolLazy.getDefaultInstance();
		consumer = RedisFactory.getConsumer(pool);
		connAdapter = new SampleConnector(pool);
		devMac = DEVINFO_PROVIDER.getMac();
	}
	private void start(){
		System.out.printf("DEVICE MAC address(设备地址): %s\n",NetworkUtil.formatMac(devMac, ":"));
		String connchname = getConnChannel(devMac);
		Channel<String> connch = new Channel<>(connchname, String.class)
				.setAdapter(connAdapter);
		consumer.register(connch);
		System.out.printf("Connect channel registered(连接频道注册) : %s \n",connchname);
	}
	public static void main(String []args){
		try{
			System.out.println("Device talk Demo starting(设备模拟器启动)");
			// 创建redis连接实例
			JedisPoolLazy.createDefaultInstance( redisParam );
			
			new Demo().start();

		}catch (Exception e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
			return ;
		}
	}

}
