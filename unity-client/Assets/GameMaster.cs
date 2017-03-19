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

using UnityEngine;

/**
 * @author L0G1C (David B) <a
 *         href=https://github.com/Binary-L0G1C/java-unity-websocket-connector>
 *         https://github.com/Binary-L0G1C/java-unity-websocket-connector </a>
 */
public class GameMaster : MonoBehaviour {

	private static GameMaster INSTANCE;

	private NetworkingController networking;

	// Create our singleton
	void Awake() {
		if (INSTANCE != null) {
			Destroy(gameObject);
		} else {
			INSTANCE = this;
			DontDestroyOnLoad(gameObject);
		}
	}

	public static NetworkingController getNetworkingController() {
		if (INSTANCE.networking == null) {
			INSTANCE.networking = new NetworkingController();
		}
		return INSTANCE.networking;
	}
}