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

using System;
using System.Collections.Generic;
using UnityEngine;
using WebSocketSharp;

/**
 * @author L0G1C (David B) <a
 *         href=https://github.com/Binary-L0G1C/java-unity-websocket-connector>
 *         https://github.com/Binary-L0G1C/java-unity-websocket-connector </a>
 */
public class NetworkingController {

	private WebSocket websocket = null;
	private Dictionary<String, HashSet<EventHandler<MessageEventArgs>>> onMessageFunctions = new Dictionary<String, HashSet<EventHandler<MessageEventArgs>>>();

	public NetworkingController() {
		Debug.Log("Networking class initiated!");
		//connectManually();
	}

	public void connect(string ip, string user, string password) {
		Debug.Log("Arg ip: " + ip);
		Debug.Log("Arg user: " + user);
		Debug.Log("Arg password: " + password);

		if (websocket != null && websocket.IsAlive) {
			Debug.LogWarning("Websocket is already connected!");
			return;
		}

		Debug.Log("Creating websocket");
		string serverAddress = "ws://" + ip + ":11037/unity-websocket-server";
		websocket = new WebSocket(serverAddress + "/unitysocket");
		websocket.Log.Level = LogLevel.Trace;
		websocket.Log.File = "D:\\somelog";

		websocket.SetCredentials(user, password, false);

		websocket.OnMessage += (sender, e) => {
			Debug.Log("Server says: " + e.Data);

			String responseType = ServerResponse.CreateFromJSON(e.Data).type;
			if (onMessageFunctions.ContainsKey(responseType)) {
				foreach (EventHandler<MessageEventArgs> eventHandler in onMessageFunctions[responseType]) {
					eventHandler.BeginInvoke(sender, e, EndAsyncEvent, null);
				}
			}
		};

		websocket.OnOpen += (sender, e) => {
			Debug.Log("Socket Open");
		};

		websocket.OnError += (sender, e) => {
			Debug.Log("Error " + e.Message);
			Debug.Log("Exception " + e.Exception);
		};

		websocket.OnClose += (sender, e) => {
			Debug.Log("Close Reason: " + e.Reason);
			Debug.Log("Close Code: " + e.Code);
			Debug.Log("Close Clean? " + e.WasClean);
		};

		Debug.Log("Connecting...");
		websocket.Connect();
		//ws.ConnectAsync();
		Debug.Log("Connection isAlive : " + websocket.IsAlive);
		Debug.Log("Connection Status : " + websocket.ReadyState);
	}


	public void startListenOnMessage(EventHandler<MessageEventArgs> funct, String responseType) {
		if (!onMessageFunctions.ContainsKey(responseType)) {
			onMessageFunctions[responseType] = new HashSet<EventHandler<MessageEventArgs>>();
		}
		onMessageFunctions[responseType].Add(funct);
	}

	public void stopListenOnMessage(EventHandler<MessageEventArgs> funct, String responseType) {
		if (onMessageFunctions.ContainsKey(responseType)) {
			onMessageFunctions[responseType].Remove(funct);
		}
	}

	public bool isConnected() {
		return websocket.IsAlive;
	}

	public void sendRequest(ClientRequest request) {
		String json = JsonUtility.ToJson(request);

		if (websocket != null) {
			Debug.Log("Sending request of type " + request.type + ": " + json);
			websocket.Send(json);
		}
	}

	/**
	 * Helper method to just connect to the server manually
	 */
	private void connectManually() {
		Debug.Log("Connecting Manually...");
		connect("127.0.0.1", "CatgirlLover", "nekomimi");
		Debug.Log("Finished Manual Connection.");
	}

	// TODO: can this be handled better?
	private void EndAsyncEvent(IAsyncResult iar) {
		var ar = (System.Runtime.Remoting.Messaging.AsyncResult)iar;
		var invokedMethod = (EventHandler)ar.AsyncDelegate;

		try {
			invokedMethod.EndInvoke(iar);
		} catch {
			// Handle any exceptions that were thrown by the invoked method
			Debug.Log("An event listener went kaboom!");
		}
	}

	public class ServerResponse {
		public string type;

		public static ServerResponse CreateFromJSON(String jsonString) {
			return JsonUtility.FromJson<ServerResponse>(jsonString);
		}
	}
}
