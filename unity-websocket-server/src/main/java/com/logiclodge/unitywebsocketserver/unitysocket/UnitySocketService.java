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

package com.logiclodge.unitywebsocketserver.unitysocket;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logiclodge.unitywebsocketserver.core.ClientEvent;
import com.logiclodge.unitywebsocketserver.core.ClientRequest;
import com.logiclodge.unitywebsocketserver.core.ClientResponse;

/**
 * @author L0G1C (David B) <a
 *         href=https://github.com/Binary-L0G1C/java-unity-websocket-connector>
 *         https://github.com/Binary-L0G1C/java-unity-websocket-connector </a>
 */
@Service
public class UnitySocketService {

	private static final Logger LOGGER = LoggerFactory.getLogger("UnitySocketService");

	private final ObjectMapper objectMapper;
	private final ApplicationEventPublisher publisher;

	@Autowired
	public UnitySocketService(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
		objectMapper = new ObjectMapper();
	}

	public void parseMessage(String messageString, WebSocketSession session) throws IOException {
		ClientRequest cr = objectMapper.readValue(messageString, ClientRequest.class);

		publisher.publishEvent(new ClientEvent<>(cr, session));
	}

	public void sendMessage(ClientResponse clientResponse, WebSocketSession session) throws IOException {
		String json = objectMapper.writeValueAsString(clientResponse);
		LOGGER.warn("sending message: {}", json);
		session.sendMessage(new TextMessage(json));
	}
}
