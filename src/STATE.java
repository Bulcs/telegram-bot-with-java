
public enum STATE {
	NULL, 
	WAITING_GOOD_NAME, WAITING_GOOD_DESCRIPTION, WAITING_GOOD_CODE,
	WAITING_LOCAL_NAME, WAITING_LOCAL_DESCRIPTION,
	WAITING_CATEGORY_NAME, WAITING_CATEGORY_DESCRIPTION, WAITING_CATEGORY_CODE,
	LIST_LOCATIONS, LIST_CATEGORIES,
	WAITING_LOCATION, WAITING_CATEGORY;
}