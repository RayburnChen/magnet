package org.nfa.athena;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;

public class TestProxy {
	
	// spring data
	// invoke oject

	@Test
	public void testProxy() {
		ProxyHandler proxyHandler = new ProxyHandler();
		Working first = (Working) proxyHandler.newProxyInstance(new AchieveFirstGoal());
		Working second = (Working) proxyHandler.newProxyInstance(new AchieveSecondGoal());
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

	public class ProxyHandler implements InvocationHandler {

		private Object realHandler;

		public Object newProxyInstance(Object realHandler) {
			this.realHandler = realHandler;
			return Proxy.newProxyInstance(realHandler.getClass().getClassLoader(), realHandler.getClass().getInterfaces(), this);
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
