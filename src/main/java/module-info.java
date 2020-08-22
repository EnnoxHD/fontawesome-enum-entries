module com.github.ennoxhd.fontawesome.enumentries {
	exports com.github.ennoxhd.fontawesome.enumentries.app;
	opens com.github.ennoxhd.fontawesome.enumentries.util to com.google.gson;
	requires com.google.gson;
}
