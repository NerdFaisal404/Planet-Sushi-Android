package fr.sushi.app.ui.payment.Global;


import fr.sushi.app.data.remote.network.ApiCall;

public class Constants {
    //FOR ADYEN PAYMENT
    public static final String CHECKOUT_SETUP = "setup";
    public static final String CHECKOUT_VERIFY = "verify";

    public static final String CHECKOUT_MERCHANT_SERVER_URL = "https://dev-ws.planetsushi.fr/app/createPaymentSession";
    public static final String CHECKOUT_MERCHANT_API_SECRET_KEY = "AQE8gXvdXN+ML0wU6mmxhmEH8LXNG9wfLbBFVXVXyGHlrmpKkMJiI9NyDj43WqOWCnD8yruhnGdX4Fh+MAMPEMFdWw2+5HzctViMSCJMYAc=-lzdXP+llhxKsUhMySOfBDQ9/Z9aNaVHM8u/FyM0BsQo=-hb9pkU3e7RS8uSUQ";
    public static final String CHECKOUT_MERCHANT_API_HEADER_KEY_FOR_API_SECRET_KEY = "x-demo-server-api-key";

}
