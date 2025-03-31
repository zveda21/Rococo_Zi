package qa.guru.rococo.api;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public enum ThreadSafeCookieStore implements CookieStore {
    INSTANCE;

    private final ThreadLocal<CookieStore> threadSafeCookieStore = ThreadLocal.withInitial(
            ThreadSafeCookieStore::inMemoryCookieStore
    );

    private static CookieStore inMemoryCookieStore() {
        return new CookieManager().getCookieStore();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        getStore().add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return getStore().get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return getStore().getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return getStore().getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return getStore().remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return getStore().removeAll();
    }

    private CookieStore getStore() {
        return threadSafeCookieStore.get();
    }

    public Optional<String> cookieValue(String cookieName) {
        return getCookies().stream()
                .filter(c -> c.getName().equals(cookieName))
                .map(HttpCookie::getValue)
                .findFirst();
    }
}
