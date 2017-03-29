package com.ronald.redis;

import java.util.*;

import org.apache.commons.lang3.SerializationUtils;
import redis.clients.jedis.Jedis;

/**
 * Redis Java驱动的使用
 * @author ronald
 * @date 2016年3月29日下午9:08:31
 * 
 */
public class TestRedis {

	private final static String ip = "192.168.48.140";
	private final static int port = 6379;
	private final static String pw="888888";

	public static void main(String[] args) {

		TestRedis tr = new TestRedis();
		Jedis jedis = getJedis();

		tr.testBasic(jedis);

		//对象序列化
//        tr.saveObj(jedis);
        tr.getObj(jedis);

        //切换数据库
//        String select = jedis.select(1);
//        System.out.println(select);

//		tr.testKeys(jedis);
		tr.testGetRedis();
    }

	public static Jedis getJedis(){
		Jedis jedis = new Jedis(ip, port);
		jedis.auth(pw);
		System.out.println("连接成功:ping:" + jedis.ping());
		return jedis;
	}

	public void testBasic(Jedis jedis){

		//设置Key
		jedis.set("mykey", "abjio");
		System.out.println("mykey："+jedis.get("mykey"));

		//List实例
		jedis.lpush("list", "张三","李四");
		List<String> list = jedis.lrange("list", 0, 1);
		for (String string : list) {
			System.out.println("list:"+string);
		}
		//获取所有的键
		Set<String> list2 = jedis.keys("*");
		for (String string : list2) {
			System.out.println("list2:" + string);
		}

		//Hash实例
		Map<String, String> map = new HashMap();
		map.put("a", "aa");
		jedis.hmset("myMap", map);
		jedis.hmset("myMap.map", map);
		List<String> hmget = jedis.hmget("myMap1", "a", "b");
		System.out.println("myMap:"+hmget);
	}

	public void saveObj(Jedis jedis){
		User u = new User();
		u.setAge(26);
		u.setBirth(new Date());
		u.setPhone("18690971234");
		u.setName("ronald");
        byte[] us = SerializationUtils.serialize(u);
        jedis.set("ronald".getBytes(), us);
    }

    public void getObj(Jedis jedis){
        byte[] bytes = jedis.get("ronald".getBytes());
        User user = SerializationUtils.deserialize(bytes);
        System.out.println("ronald:"+user);
    }

    public void testKeys(Jedis jedis){
		List<String> mget = jedis.mget("foo2", "foo", "m3");
		for (String m: mget) {
			System.out.println(m);
		}
	}

	public void testGetRedis(){
		long l = System.currentTimeMillis();
		for (int i = 0; i< 1000; i++){
			//从连接池中获取Jedis实例比直接new() 要快
			Jedis jedis = JedisPoolUtil.getJedis();

//			Jedis jedis = getJedis();
			if(jedis != null){
				String foo = jedis.get("foo");

				System.out.println("获取Redis成功！" + i);
				System.out.println("foo:" + foo);
			} else{
				System.err.println("获取Redis失败！" + i);

			}
			JedisPoolUtil.close(jedis);
			System.out.println(jedis == null);
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		long l1 = System.currentTimeMillis();
		System.err.println("运行时间：" + (l1 - l) + "ms");
	}
}
