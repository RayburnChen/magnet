package org.nfa.athena;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;

public class TestProxy {

	/*
	 * Spring data proxy repository to PartTreeMongoQuery And generate Query
	 * according to method names and input params
	 * org.springframework.data.mongodb.repository.query.AbstractMongoQuery
	 * org.springframework.data.mongodb.repository.query.PartTreeMongoQuery
	 */

	/*
	 * invoke object The denamic proxy object generate by JDK
	 */

	/*
	 * theory of Proxy.newProxyInstance 
	 * Class<?> cl = getProxyClass0(loader, * interfaces); 
	 * Constructor<?> cons = cl.getConstructor(constructorParams);
	 * return cons.newInstance(new Object[]{invocationHandler});
	 */

	@Test
	public void testProxy() {
		Working first = (Working) ProxyHandler.newProxyInstance(new AchieveFirstGoal());
		Working second = (Working) ProxyHandler.newProxyInstance(new AchieveSecondGoal());
		first.working();
		second.working();
	}

	public interface Working {
		public void working();
	}

	public class AchieveFirstGoal implements Working {

		@Override
		public void working() {
			System.err.println("TestProxy.AchieveFirstGoal.working()");
		}

	}

	public class AchieveSecondGoal implements Working {

		@Override
		public void working() {
			System.err.println("TestProxy.AchieveSecondGoal.working()");
		}

	}

	public static class ProxyHandler implements InvocationHandler {

		private final Object realHandler;

		public ProxyHandler(Object realHandler) {
			super();
			this.realHandler = realHandler;
		}

		public static Object newProxyInstance(Object realHandler) {
			return Proxy.newProxyInstance(realHandler.getClass().getClassLoader(), realHandler.getClass().getInterfaces(), new ProxyHandler(realHandler));
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			System.out.println(proxy.getClass());
			System.out.println("Start TestProxy.ProxyHandler.invoke()");
			Object result = method.invoke(realHandler, args);
			System.out.println("End TestProxy.ProxyHandler.invoke()");
			return result;
		}

	}

}
