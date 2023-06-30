package org.example.processor;

import org.example.service.CustomerService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// ОБРАБОТЧИК АННОТАЦИИ @Benchmark - БИН ПОСТ ПРОЦЕССОР
// V1
//@Component
//public class BenchmarkBeanPostProcessor implements BeanPostProcessor {
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        // ЕСЛИ АННОТАЦИЯ СТОИТ НАД КЛАССОМ ИЛИ МЕТОДОМ ВЫВОДИТСЯ СООБЩЕНИЕ
//        if (bean.getClass().isAnnotationPresent(Benchmark.class) || isMethodAnnotated(bean.getClass())) {
//            System.out.printf("<<< before init %s <<< Benchmark started %n", beanName);
//        }
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        System.out.printf(">>> after init %s%n", beanName);
//        return bean;
//    }
//
//    // МЕТОД ПРОВЕРКИ - СТОИТ ЛИ АННОТАЦИЯ НАД МЕТОДОМ
//    public boolean isMethodAnnotated(Class<?> aClass) {
//      return Arrays.stream(aClass.getMethods()).anyMatch(m->m.isAnnotationPresent(Benchmark.class));
//    }
//}

// V2
//@Component
//public class BenchmarkBeanPostProcessor implements BeanPostProcessor {
//    private Map<String, Class<?>> map = new HashMap<>();
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        // ЕСЛИ АННОТАЦИЯ СТОИТ НАД КЛАССОМ ИЛИ МЕТОДОМ ВЫВОДИТСЯ СООБЩЕНИЕ
//        Class<?> aClass = bean.getClass();
//        if (bean.getClass().isAnnotationPresent(Benchmark.class) || isMethodAnnotated(bean.getClass())) {
//            map.put(beanName, aClass); // КЛАДЕМ В МАПУ КЛАСС ЕСЛИ ЕГО ТАМ ЕЩЕ НЕТ И ОН ПОМЕЧЕН АННОТАЦИЕЙ
//            System.out.printf("<<< before init %s <<< Benchmark started %n", beanName);
//        }
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if(map.get(beanName)!=null) { // ЕСЛМ В МАПЕ ЕСТЬ ЧТО-ТО, ТО ТОЛЬКО НА НЕГО ВЫВОДИМ After
//            System.out.printf(">>> after init %s%n", beanName);
//        }
//        return bean;
//    }
//
//    // МЕТОД ПРОВЕРКИ - СТОИТ ЛИ АННОТАЦИЯ НАД МЕТОДОМ
//    public boolean isMethodAnnotated(Class<?> aClass) {
//        return Arrays.stream(aClass.getMethods()).anyMatch(m->m.isAnnotationPresent(Benchmark.class));
//    }
//}

//V3

@Component
public class BenchmarkBeanPostProcessor implements BeanPostProcessor {
    private Map<String, Class<?>> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
	// ЕСЛИ АННОТАЦИЯ СТОИТ НАД КЛАССОМ ИЛИ МЕТОДОМ ВЫВОДИТСЯ СООБЩЕНИЕ
	Class<?> aClass = bean.getClass();
	if (bean.getClass().isAnnotationPresent(Benchmark.class) || isMethodAnnotated(bean.getClass())) {
	    map.put(beanName, aClass); // КЛАДЕМ В МАПУ КЛАСС ЕСЛИ ЕГО ТАМ ЕЩЕ НЕТ И ОН ПОМЕЧЕН АННОТАЦИЕЙ
	    System.out.printf("<<< before init %s <<< Benchmark started %n", beanName);
	}
	return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
	if (map.get(beanName) != null) { // ЕСЛМ В МАПЕ ЕСТЬ ЧТО-ТО, ТО ТОЛЬКО НА НЕГО ВЫВОДИМ After

	    bean = prxoy(bean);

	    System.out.printf(">>> after init %s%n", beanName);
	}
	return bean;
    }

    private Object prxoy(Object bean) {
	// НАЧАЛО ОБРАБОТЧИКА
	MethodInterceptor handler = (obj, method, args, proxy) -> {
	    Object result;
	    if (bean.getClass().isAnnotationPresent(Benchmark.class) || method.isAnnotationPresent(Benchmark.class)) {
		// можно чтото делать до вызова метода, помеченного анотацией

		System.out.printf("==== Method %s started ====%n", method.getName());
		long time = System.nanoTime();
		result = proxy.invoke(bean, args);
		// можно чтото делать после вызова метода, помеченного анотацией, например транзакцию
		time = System.nanoTime() - time;
		System.out.printf("==== Method %s complete. Time = %d ms ==== %n%n", method.getName(), TimeUnit.NANOSECONDS.toMillis(time));
	    } else {
		result = proxy.invoke(bean, args);
	    }
	    return result;
	};
	// КОНЕЦ ОБРАБОТЧИКА

	Object proxy = Enhancer.create(bean.getClass(), handler);
	System.out.println("here proxy for " + bean);
	return proxy;
    }

    // МЕТОД ПРОВЕРКИ - СТОИТ ЛИ АННОТАЦИЯ НАД МЕТОДОМ
    public boolean isMethodAnnotated(Class<?> aClass) {
	return Arrays.stream(aClass.getMethods()).anyMatch(m -> m.isAnnotationPresent(Benchmark.class));
    }
}