package com.example.demo;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.cloud.spanner.Key;
import com.google.cloud.spring.data.spanner.core.SpannerTemplate;
import com.google.cloud.spring.data.spanner.core.admin.SpannerDatabaseAdminTemplate;
import com.google.cloud.spring.data.spanner.core.admin.SpannerSchemaUtils;

@SpringBootApplication
public class DemoApplication {
	@Autowired
	private SpannerDatabaseAdminTemplate spannerDatabaseAdminTemplate;

	@Autowired
	private SpannerSchemaUtils spannerSchemaUtils;

	@Autowired
	private SpannerTemplate spannerTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@PostConstruct
	void example() {
		System.out.println("EXAMPLE STARTED");

		// make the users table
		System.out.println("CREATING USERS TABLE");
		createTableIfNeeded("users", User.class);

		// put an example user in the database
		System.out.println("ADDING INITIAL VALUE");
		final User user = new User();
		user.id = UUID.randomUUID().toString();
		user.credits = 10;
		spannerTemplate.upsert(user);
		User dbUser = spannerTemplate.read(User.class, Key.of(user.id));
		assert dbUser.credits == 10;

		// try a partial update of the user without including their ID in the
		// includeProperties and show that the user is not updated
		System.out.println("TRYING UPDATE WITHOUT PRIMARY KEYS");
		user.credits = 20;
		try {
			spannerTemplate.update(user, Set.of("credits"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		dbUser = spannerTemplate.read(User.class, Key.of(user.id));
		assert dbUser.credits == 10;

		// do a partial update of the user while including their ID in the
		// includeProperties and show that the user is updated
		System.out.println("TRYING UPDATE WITH PRIMARY KEYS");
		user.credits = 30;
		spannerTemplate.update(user, Set.of("id", "credits"));
		dbUser = spannerTemplate.read(User.class, Key.of(user.id));
		assert dbUser.credits == 30;

		System.out.println("EXAMPLE DONE");
	}

	private void createTableIfNeeded(String tableName, Class<?> tableClass) {
		if (!spannerDatabaseAdminTemplate.tableExists(tableName)) {
			final String statement = spannerSchemaUtils.getCreateTableDdlString(tableClass);
			spannerDatabaseAdminTemplate.executeDdlStrings(
					List.of(statement), true);
		}
	}
}
