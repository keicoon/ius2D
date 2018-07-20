package ius;

import android.util.Log;

public class ObjectManager {

	public int default_INDEX = 100;

	// use object pool
	// using DS 'LinkedList'

	class Item {
		// public boolean active; // ????
		public GameObject gameObject; // Item
		private Item nextItem; // nextLink

		public Item(GameObject data) {
			// this.active = false;
			this.gameObject = data;
			this.nextItem = null;
		}
	}

	private Item USE_object_head; // 사용하고 있는 오브젝트의 LL
	private Item NONUSE_object_head; // 사용하지 않는 오브젝트의 LL

	/* 초기화 */
	private static ObjectManager instance;

	public static ObjectManager getInstance() {
		if (instance == null) {
			instance = new ObjectManager();
		}
		return instance;
	}

	private ObjectManager() {
		USE_object_head = new Item(null);
		NONUSE_object_head = new Item(null);
	}

	private void UaddFirst(Item newItem) {
		newItem.nextItem = USE_object_head.nextItem;
		USE_object_head.nextItem = newItem;
	}

	private void NaddFirst(Item newItem) {
		newItem.nextItem = NONUSE_object_head.nextItem;
		NONUSE_object_head.nextItem = newItem;
	}

	/* List를 처음 생성할 때 사용하는 함수 */
	private void addFirst(GameObject data) {
		Item newItem = new Item(data);

		newItem.nextItem = NONUSE_object_head.nextItem;
		NONUSE_object_head.nextItem = newItem;
	}

	public void Create(int counter) {
		for (int i = 0; i < counter; i++) {
			GameObject object = new GameObject();
			addFirst(object);
		}
	}

	public GameObject newItem(
			String textureName, int p_AN, int p_SN, float px, float py,
			float pangle, float pscale) {
		if (NONUSE_object_head == null)
			return null;

		Item prevItem = NONUSE_object_head;
		// 남은 리스트가 없는 경우 INDEX수 만큼 리스트에 추가한다.
		if (prevItem.nextItem == null) {
			Log.d("object", "리스트가 " + default_INDEX + " 수 만큼 추가 생성됨");
			Create(default_INDEX);
		}
		/*
		 * for(Item tmpItem = prevItem.nextItem;tmpItem != null ; tmpItem =
		 * tmpItem.nextItem){ if(tmpItem.active == false){ prevItem.nextItem =
		 * tmpItem.nextItem; UaddFirst(tmpItem);
		 * tmpItem.gameObject.SetGameObject(mContext, mGL20, textureName, p_AN,
		 * p_SN, px, py, pangle, pscale); return tmpItem.gameObject; } prevItem
		 * = tmpItem; } return null;
		 */
		Item tmpItem = prevItem.nextItem;
		prevItem.nextItem = tmpItem.nextItem;
		UaddFirst(tmpItem);
		tmpItem.gameObject.SetGameObject(textureName, p_AN,
				p_SN, px, py, pangle, pscale);
		return tmpItem.gameObject;
	}

	public void RemoveItem(GameObject gameObject) {
		if (USE_object_head == null || gameObject == null)
			return;

		Item prevItem = USE_object_head;
		for (Item tmpItem = prevItem.nextItem; tmpItem != null; tmpItem = tmpItem.nextItem) {
			if (tmpItem.gameObject == gameObject) {
				// tmpItem.active = false;
				prevItem.nextItem = tmpItem.nextItem;
				NaddFirst(tmpItem);
				break;
			}
			prevItem = tmpItem;
		}
	}

	public void ClearItem() {
		Log.d("object", "리스트가 정리됨");
		Item prevItem = USE_object_head;
		for (Item tmpItem = prevItem.nextItem; tmpItem != null;) {
			// tmpItem.active = false;
			prevItem.nextItem = tmpItem.nextItem;
			NaddFirst(tmpItem);
			tmpItem = prevItem.nextItem;
		}
	}

	public void Dispose() {
		Log.d("object", "리스트가 삭제됨");
		if (USE_object_head == null && NONUSE_object_head == null)
			return;

		ClearItem();
		Item nextItem = NONUSE_object_head;
		for (Item tmpItem = nextItem.nextItem; tmpItem != null; tmpItem = tmpItem.nextItem) {
			nextItem = tmpItem.nextItem;
			if (tmpItem != null) {
				tmpItem.gameObject = null;
				tmpItem = null;
			}
			tmpItem = nextItem;
		}
		USE_object_head.nextItem = null;
		NONUSE_object_head.nextItem = null;
	}
}
