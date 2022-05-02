package project.OurRecipe.Config.Oauth.provider;

public interface OAuth2UserInfo {
		String getProviderId();
		String getProvider();
		String getEmail();
		String getName();
}
