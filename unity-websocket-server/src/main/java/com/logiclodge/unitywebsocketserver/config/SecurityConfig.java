/*
 * Copyright 2017 L0G1C (David B) - logiclodge.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.logiclodge.unitywebsocketserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.logiclodge.unitywebsocketserver.user.User;
import com.logiclodge.unitywebsocketserver.user.UserDao;

/**
 * @author L0G1C (David B) <a
 *         href=https://github.com/Binary-L0G1C/java-unity-websocket-connector>
 *         https://github.com/Binary-L0G1C/java-unity-websocket-connector </a>
 */
@Configuration
@ComponentScan("com.logiclodge.unitywebsocketserver")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDao userDao;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		for (User user : userDao.getUsers()) {
			auth.inMemoryAuthentication().withUser(user.getUsername()).password(user.getPassword()).roles("USER");
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() //
				.authorizeRequests().antMatchers("/**").access("hasRole('ROLE_USER')") //
				.and().httpBasic();
	}
}
