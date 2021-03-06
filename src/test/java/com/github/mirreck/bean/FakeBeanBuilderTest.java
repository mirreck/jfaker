package com.github.mirreck.bean;

import static org.fest.assertions.Assertions.*;

import com.github.mirreck.FakeFactory;
import com.github.mirreck.bean.domain.Task;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mirreck.bean.domain.Person;

import java.util.Random;

public class FakeBeanBuilderTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FakeBeanBuilderTest.class);

	
	@Test
	public void should_build_with_given_configuration() {
		FakeBeanBuilder<Person> fbb = new FakeBeanBuilder<Person>(Person.class)
            .withParameterSequence("id")
			.withParameterPattern("job", "abc")
            .withParameterPattern("birthDate", "{{date 1900 2010}}");
		
		Person person = fbb.build();
        // filled
		assertThat(person.getId()).isNotNull().isPositive();
        assertThat(person.getJob()).isNotEmpty().isEqualTo("abc");
        assertThat(person.getBirthDate()).isNotNull();

        // NOT filled
        assertThat(person.getAddress()).isNull();
        LOGGER.info("bean :" + person);
	}

    @Test
    public void should_build_with_default_file_configuration() {
        FakeBeanBuilder<Person> fbb = new FakeBeanBuilder<Person>(Person.class)
                .initWithConfigurationFile();

        Person person = fbb.build();
        assertThat(person.getId()).isNotNull().isPositive();
        assertThat(person.getJob()).isNotEmpty();
        assertThat(person.getBirthDate()).isNotNull();
        assertThat(person.getAddress()).isNotNull();

        LOGGER.info("bean :" + person);
    }

    @Test
    public void should_build_with_another_file_configuration() {
        FakeBeanBuilder<Person> fbb = new FakeBeanBuilder<Person>(Person.class)
                .initWithConfigurationFile("/conf/fake-factory/another_cfg.yml");

        Person person = fbb.build();
        assertThat(person.getId()).isNotNull().isPositive();
        assertThat(person.getJob()).isNotEmpty();
        assertThat(person.getBirthDate()).isNotNull();
        assertThat(person.getAddress()).isNotNull();
        LOGGER.info("bean :" + person);
    }

    @Test
    @Ignore("not working yet...")
    public void should_build_with_guessed_configuration() {
        FakeBeanBuilder<Person> fbb = new FakeBeanBuilder<Person>(Person.class)
                .withGuessedFillers();

        Person person = fbb.build();
        assertThat(person.getId()).isNotNull().isPositive();
        assertThat(person.getJob()).isNotEmpty();
        assertThat(person.getBirthDate()).isNotNull();
        LOGGER.info("bean :" + person);
    }


    @Test
    public void should_build_with_pooled_objects_configuration() {
        final FakeFactory fakeFactory = new FakeFactory();

        final BeanPool beanPool = new BeanPool(fakeFactory.getRandom());

        FakeBeanBuilder<Task> taskBuilder = new FakeBeanBuilder<Task>(Task.class, fakeFactory, beanPool)
                .initWithConfigurationFile();

        FakeBeanBuilder<Person> personBuilder = new FakeBeanBuilder<Person>(Person.class, fakeFactory, beanPool)
                .initWithConfigurationFile()
                .withParameterPool("task");

        taskBuilder.build(5);
        Person person = personBuilder.build();


        assertThat(person.getTask()).isNotNull();
        LOGGER.info("bean :" + person);
    }
}
