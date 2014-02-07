package at.bestsolution.wgraf.style;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class FontAwesome {

	public static final FontSource FONTAWESOME;
	static {
		FontSource u;
		try {
			u = new FontSource(new URI("platform:/plugin/at.bestsolution.wgraf/fonts/fontawesome-webfont.ttf"));
		} catch (URISyntaxException e) {
			u = null;
		}
		FONTAWESOME = u;
	}

	public static Map<String, String> MAP = init();

	private static Map<String, String> init() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("fa-glass", "\uf000");

		map.put("fa-music", "\uf001");

		map.put("fa-search", "\uf002");

		map.put("fa-envelope-o", "\uf003");

		map.put("fa-heart", "\uf004");

		map.put("fa-star", "\uf005");

		map.put("fa-star-o", "\uf006");

		map.put("fa-user", "\uf007");

		map.put("fa-film", "\uf008");

		map.put("fa-th-large", "\uf009");

		map.put("fa-th", "\uf00a");

		map.put("fa-th-list", "\uf00b");

		map.put("fa-check", "\uf00c");

		map.put("fa-times", "\uf00d");

		map.put("fa-search-plus", "\uf00e");

		map.put("fa-search-minus", "\uf010");

		map.put("fa-power-off", "\uf011");

		map.put("fa-signal", "\uf012");

		map.put("fa-cog", "\uf013");

		map.put("fa-trash-o", "\uf014");

		map.put("fa-home", "\uf015");

		map.put("fa-file-o", "\uf016");

		map.put("fa-clock-o", "\uf017");

		map.put("fa-road", "\uf018");

		map.put("fa-download", "\uf019");

		map.put("fa-arrow-circle-o-down", "\uf01a");

		map.put("fa-arrow-circle-o-up", "\uf01b");

		map.put("fa-inbox", "\uf01c");

		map.put("fa-play-circle-o", "\uf01d");

		map.put("fa-repeat", "\uf01e");

		map.put("fa-refresh", "\uf021");

		map.put("fa-list-alt", "\uf022");

		map.put("fa-lock", "\uf023");

		map.put("fa-flag", "\uf024");

		map.put("fa-headphones", "\uf025");

		map.put("fa-volume-off", "\uf026");

		map.put("fa-volume-down", "\uf027");

		map.put("fa-volume-up", "\uf028");

		map.put("fa-qrcode", "\uf029");

		map.put("fa-barcode", "\uf02a");

		map.put("fa-tag", "\uf02b");

		map.put("fa-tags", "\uf02c");

		map.put("fa-book", "\uf02d");

		map.put("fa-bookmark", "\uf02e");

		map.put("fa-print", "\uf02f");

		map.put("fa-camera", "\uf030");

		map.put("fa-font", "\uf031");

		map.put("fa-bold", "\uf032");

		map.put("fa-italic", "\uf033");

		map.put("fa-text-height", "\uf034");

		map.put("fa-text-width", "\uf035");

		map.put("fa-align-left", "\uf036");

		map.put("fa-align-center", "\uf037");

		map.put("fa-align-right", "\uf038");

		map.put("fa-align-justify", "\uf039");

		map.put("fa-list", "\uf03a");

		map.put("fa-outdent", "\uf03b");

		map.put("fa-indent", "\uf03c");

		map.put("fa-video-camera", "\uf03d");

		map.put("fa-picture-o", "\uf03e");

		map.put("fa-pencil", "\uf040");

		map.put("fa-map-marker", "\uf041");

		map.put("fa-adjust", "\uf042");

		map.put("fa-tint", "\uf043");

		map.put("fa-pencil-square-o", "\uf044");

		map.put("fa-share-square-o", "\uf045");

		map.put("fa-check-square-o", "\uf046");

		map.put("fa-arrows", "\uf047");

		map.put("fa-step-backward", "\uf048");

		map.put("fa-fast-backward", "\uf049");

		map.put("fa-backward", "\uf04a");

		map.put("fa-play", "\uf04b");

		map.put("fa-pause", "\uf04c");

		map.put("fa-stop", "\uf04d");

		map.put("fa-forward", "\uf04e");

		map.put("fa-fast-forward", "\uf050");

		map.put("fa-step-forward", "\uf051");

		map.put("fa-eject", "\uf052");

		map.put("fa-chevron-left", "\uf053");

		map.put("fa-chevron-right", "\uf054");

		map.put("fa-plus-circle", "\uf055");

		map.put("fa-minus-circle", "\uf056");

		map.put("fa-times-circle", "\uf057");

		map.put("fa-check-circle", "\uf058");

		map.put("fa-question-circle", "\uf059");

		map.put("fa-info-circle", "\uf05a");

		map.put("fa-crosshairs", "\uf05b");

		map.put("fa-times-circle-o", "\uf05c");

		map.put("fa-check-circle-o", "\uf05d");

		map.put("fa-ban", "\uf05e");

		map.put("fa-arrow-left", "\uf060");

		map.put("fa-arrow-right", "\uf061");

		map.put("fa-arrow-up", "\uf062");

		map.put("fa-arrow-down", "\uf063");

		map.put("fa-share", "\uf064");

		map.put("fa-expand", "\uf065");

		map.put("fa-compress", "\uf066");

		map.put("fa-plus", "\uf067");

		map.put("fa-minus", "\uf068");

		map.put("fa-asterisk", "\uf069");

		map.put("fa-exclamation-circle", "\uf06a");

		map.put("fa-gift", "\uf06b");

		map.put("fa-leaf", "\uf06c");

		map.put("fa-fire", "\uf06d");

		map.put("fa-eye", "\uf06e");

		map.put("fa-eye-slash", "\uf070");

		map.put("fa-exclamation-triangle", "\uf071");

		map.put("fa-plane", "\uf072");

		map.put("fa-calendar", "\uf073");

		map.put("fa-random", "\uf074");

		map.put("fa-comment", "\uf075");

		map.put("fa-magnet", "\uf076");

		map.put("fa-chevron-up", "\uf077");

		map.put("fa-chevron-down", "\uf078");

		map.put("fa-retweet", "\uf079");

		map.put("fa-shopping-cart", "\uf07a");

		map.put("fa-folder", "\uf07b");

		map.put("fa-folder-open", "\uf07c");

		map.put("fa-arrows-v", "\uf07d");

		map.put("fa-arrows-h", "\uf07e");

		map.put("fa-bar-chart-o", "\uf080");

		map.put("fa-twitter-square", "\uf081");

		map.put("fa-facebook-square", "\uf082");

		map.put("fa-camera-retro", "\uf083");

		map.put("fa-key", "\uf084");

		map.put("fa-cogs", "\uf085");

		map.put("fa-comments", "\uf086");

		map.put("fa-thumbs-o-up", "\uf087");

		map.put("fa-thumbs-o-down", "\uf088");

		map.put("fa-star-half", "\uf089");

		map.put("fa-heart-o", "\uf08a");

		map.put("fa-sign-out", "\uf08b");

		map.put("fa-linkedin-square", "\uf08c");

		map.put("fa-thumb-tack", "\uf08d");

		map.put("fa-external-link", "\uf08e");

		map.put("fa-sign-in", "\uf090");

		map.put("fa-trophy", "\uf091");

		map.put("fa-github-square", "\uf092");

		map.put("fa-upload", "\uf093");

		map.put("fa-lemon-o", "\uf094");

		map.put("fa-phone", "\uf095");

		map.put("fa-square-o", "\uf096");

		map.put("fa-bookmark-o", "\uf097");

		map.put("fa-phone-square", "\uf098");

		map.put("fa-twitter", "\uf099");

		map.put("fa-facebook", "\uf09a");

		map.put("fa-github", "\uf09b");

		map.put("fa-unlock", "\uf09c");

		map.put("fa-credit-card", "\uf09d");

		map.put("fa-rss", "\uf09e");

		map.put("fa-hdd-o", "\uf0a0");

		map.put("fa-bullhorn", "\uf0a1");

		map.put("fa-bell", "\uf0f3");

		map.put("fa-certificate", "\uf0a3");

		map.put("fa-hand-o-right", "\uf0a4");

		map.put("fa-hand-o-left", "\uf0a5");

		map.put("fa-hand-o-up", "\uf0a6");

		map.put("fa-hand-o-down", "\uf0a7");

		map.put("fa-arrow-circle-left", "\uf0a8");

		map.put("fa-arrow-circle-right", "\uf0a9");

		map.put("fa-arrow-circle-up", "\uf0aa");

		map.put("fa-arrow-circle-down", "\uf0ab");

		map.put("fa-globe", "\uf0ac");

		map.put("fa-wrench", "\uf0ad");

		map.put("fa-tasks", "\uf0ae");

		map.put("fa-filter", "\uf0b0");

		map.put("fa-briefcase", "\uf0b1");

		map.put("fa-arrows-alt", "\uf0b2");

		map.put("fa-users", "\uf0c0");

		map.put("fa-link", "\uf0c1");

		map.put("fa-cloud", "\uf0c2");

		map.put("fa-flask", "\uf0c3");

		map.put("fa-scissors", "\uf0c4");

		map.put("fa-files-o", "\uf0c5");

		map.put("fa-paperclip", "\uf0c6");

		map.put("fa-floppy-o", "\uf0c7");

		map.put("fa-square", "\uf0c8");

		map.put("fa-bars", "\uf0c9");

		map.put("fa-list-ul", "\uf0ca");

		map.put("fa-list-ol", "\uf0cb");

		map.put("fa-strikethrough", "\uf0cc");

		map.put("fa-underline", "\uf0cd");

		map.put("fa-table", "\uf0ce");

		map.put("fa-magic", "\uf0d0");

		map.put("fa-truck", "\uf0d1");

		map.put("fa-pinterest", "\uf0d2");

		map.put("fa-pinterest-square", "\uf0d3");

		map.put("fa-google-plus-square", "\uf0d4");

		map.put("fa-google-plus", "\uf0d5");

		map.put("fa-money", "\uf0d6");

		map.put("fa-caret-down", "\uf0d7");

		map.put("fa-caret-up", "\uf0d8");

		map.put("fa-caret-left", "\uf0d9");

		map.put("fa-caret-right", "\uf0da");

		map.put("fa-columns", "\uf0db");

		map.put("fa-sort", "\uf0dc");

		map.put("fa-sort-asc", "\uf0dd");

		map.put("fa-sort-desc", "\uf0de");

		map.put("fa-envelope", "\uf0e0");

		map.put("fa-linkedin", "\uf0e1");

		map.put("fa-undo", "\uf0e2");

		map.put("fa-gavel", "\uf0e3");

		map.put("fa-tachometer", "\uf0e4");

		map.put("fa-comment-o", "\uf0e5");

		map.put("fa-comments-o", "\uf0e6");

		map.put("fa-bolt", "\uf0e7");

		map.put("fa-sitemap", "\uf0e8");

		map.put("fa-umbrella", "\uf0e9");

		map.put("fa-clipboard", "\uf0ea");

		map.put("fa-lightbulb-o", "\uf0eb");

		map.put("fa-exchange", "\uf0ec");

		map.put("fa-cloud-download", "\uf0ed");

		map.put("fa-cloud-upload", "\uf0ee");

		map.put("fa-user-md", "\uf0f0");

		map.put("fa-stethoscope", "\uf0f1");

		map.put("fa-suitcase", "\uf0f2");

		map.put("fa-bell-o", "\uf0a2");

		map.put("fa-coffee", "\uf0f4");

		map.put("fa-cutlery", "\uf0f5");

		map.put("fa-file-text-o", "\uf0f6");

		map.put("fa-building-o", "\uf0f7");

		map.put("fa-hospital-o", "\uf0f8");

		map.put("fa-ambulance", "\uf0f9");

		map.put("fa-medkit", "\uf0fa");

		map.put("fa-fighter-jet", "\uf0fb");

		map.put("fa-beer", "\uf0fc");

		map.put("fa-h-square", "\uf0fd");

		map.put("fa-plus-square", "\uf0fe");

		map.put("fa-angle-double-left", "\uf100");

		map.put("fa-angle-double-right", "\uf101");

		map.put("fa-angle-double-up", "\uf102");

		map.put("fa-angle-double-down", "\uf103");

		map.put("fa-angle-left", "\uf104");

		map.put("fa-angle-right", "\uf105");

		map.put("fa-angle-up", "\uf106");

		map.put("fa-angle-down", "\uf107");

		map.put("fa-desktop", "\uf108");

		map.put("fa-laptop", "\uf109");

		map.put("fa-tablet", "\uf10a");

		map.put("fa-mobile", "\uf10b");

		map.put("fa-circle-o", "\uf10c");

		map.put("fa-quote-left", "\uf10d");

		map.put("fa-quote-right", "\uf10e");

		map.put("fa-spinner", "\uf110");

		map.put("fa-circle", "\uf111");

		map.put("fa-reply", "\uf112");

		map.put("fa-github-alt", "\uf113");

		map.put("fa-folder-o", "\uf114");

		map.put("fa-folder-open-o", "\uf115");

		map.put("fa-smile-o", "\uf118");

		map.put("fa-frown-o", "\uf119");

		map.put("fa-meh-o", "\uf11a");

		map.put("fa-gamepad", "\uf11b");

		map.put("fa-keyboard-o", "\uf11c");

		map.put("fa-flag-o", "\uf11d");

		map.put("fa-flag-checkered", "\uf11e");

		map.put("fa-terminal", "\uf120");

		map.put("fa-code", "\uf121");

		map.put("fa-reply-all", "\uf122");

		map.put("fa-mail-reply-all", "\uf122");

		map.put("fa-star-half-o", "\uf123");

		map.put("fa-location-arrow", "\uf124");

		map.put("fa-crop", "\uf125");

		map.put("fa-code-fork", "\uf126");

		map.put("fa-chain-broken", "\uf127");

		map.put("fa-question", "\uf128");

		map.put("fa-info", "\uf129");

		map.put("fa-exclamation", "\uf12a");

		map.put("fa-superscript", "\uf12b");

		map.put("fa-subscript", "\uf12c");

		map.put("fa-eraser", "\uf12d");

		map.put("fa-puzzle-piece", "\uf12e");

		map.put("fa-microphone", "\uf130");

		map.put("fa-microphone-slash", "\uf131");

		map.put("fa-shield", "\uf132");

		map.put("fa-calendar-o", "\uf133");

		map.put("fa-fire-extinguisher", "\uf134");

		map.put("fa-rocket", "\uf135");

		map.put("fa-maxcdn", "\uf136");

		map.put("fa-chevron-circle-left", "\uf137");

		map.put("fa-chevron-circle-right", "\uf138");

		map.put("fa-chevron-circle-up", "\uf139");

		map.put("fa-chevron-circle-down", "\uf13a");

		map.put("fa-html5", "\uf13b");

		map.put("fa-css3", "\uf13c");

		map.put("fa-anchor", "\uf13d");

		map.put("fa-unlock-alt", "\uf13e");

		map.put("fa-bullseye", "\uf140");

		map.put("fa-ellipsis-h", "\uf141");

		map.put("fa-ellipsis-v", "\uf142");

		map.put("fa-rss-square", "\uf143");

		map.put("fa-play-circle", "\uf144");

		map.put("fa-ticket", "\uf145");

		map.put("fa-minus-square", "\uf146");

		map.put("fa-minus-square-o", "\uf147");

		map.put("fa-level-up", "\uf148");

		map.put("fa-level-down", "\uf149");

		map.put("fa-check-square", "\uf14a");

		map.put("fa-pencil-square", "\uf14b");

		map.put("fa-external-link-square", "\uf14c");

		map.put("fa-share-square", "\uf14d");

		map.put("fa-compass", "\uf14e");

		map.put("fa-caret-square-o-down", "\uf150");

		map.put("fa-caret-square-o-up", "\uf151");

		map.put("fa-caret-square-o-right", "\uf152");

		map.put("fa-eur", "\uf153");

		map.put("fa-gbp", "\uf154");

		map.put("fa-usd", "\uf155");

		map.put("fa-inr", "\uf156");

		map.put("fa-jpy", "\uf157");

		map.put("fa-rub", "\uf158");

		map.put("fa-krw", "\uf159");

		map.put("fa-btc", "\uf15a");

		map.put("fa-file", "\uf15b");

		map.put("fa-file-text", "\uf15c");

		map.put("fa-sort-alpha-asc", "\uf15d");

		map.put("fa-sort-alpha-desc", "\uf15e");

		map.put("fa-sort-amount-asc", "\uf160");

		map.put("fa-sort-amount-desc", "\uf161");

		map.put("fa-sort-numeric-asc", "\uf162");

		map.put("fa-sort-numeric-desc", "\uf163");

		map.put("fa-thumbs-up", "\uf164");

		map.put("fa-thumbs-down", "\uf165");

		map.put("fa-youtube-square", "\uf166");

		map.put("fa-youtube", "\uf167");

		map.put("fa-xing", "\uf168");

		map.put("fa-xing-square", "\uf169");

		map.put("fa-youtube-play", "\uf16a");

		map.put("fa-dropbox", "\uf16b");

		map.put("fa-stack-overflow", "\uf16c");

		map.put("fa-instagram", "\uf16d");

		map.put("fa-flickr", "\uf16e");

		map.put("fa-adn", "\uf170");

		map.put("fa-bitbucket", "\uf171");

		map.put("fa-bitbucket-square", "\uf172");

		map.put("fa-tumblr", "\uf173");

		map.put("fa-tumblr-square", "\uf174");

		map.put("fa-long-arrow-down", "\uf175");

		map.put("fa-long-arrow-up", "\uf176");

		map.put("fa-long-arrow-left", "\uf177");

		map.put("fa-long-arrow-right", "\uf178");

		map.put("fa-apple", "\uf179");

		map.put("fa-windows", "\uf17a");

		map.put("fa-android", "\uf17b");

		map.put("fa-linux", "\uf17c");

		map.put("fa-dribbble", "\uf17d");

		map.put("fa-skype", "\uf17e");

		map.put("fa-foursquare", "\uf180");

		map.put("fa-trello", "\uf181");

		map.put("fa-female", "\uf182");

		map.put("fa-male", "\uf183");

		map.put("fa-gittip", "\uf184");

		map.put("fa-sun-o", "\uf185");

		map.put("fa-moon-o", "\uf186");

		map.put("fa-archive", "\uf187");

		map.put("fa-bug", "\uf188");

		map.put("fa-vk", "\uf189");

		map.put("fa-weibo", "\uf18a");

		map.put("fa-renren", "\uf18b");

		map.put("fa-pagelines", "\uf18c");

		map.put("fa-stack-exchange", "\uf18d");

		map.put("fa-arrow-circle-o-right", "\uf18e");

		map.put("fa-arrow-circle-o-left", "\uf190");

		map.put("fa-caret-square-o-left", "\uf191");

		map.put("fa-dot-circle-o", "\uf192");

		map.put("fa-wheelchair", "\uf193");

		map.put("fa-vimeo-square", "\uf194");

		map.put("fa-try", "\uf195");

		map.put("fa-plus-square-o", "\uf196");
		return map;
	}
}
