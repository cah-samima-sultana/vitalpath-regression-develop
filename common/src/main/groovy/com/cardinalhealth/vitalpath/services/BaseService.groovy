package com.cardinalhealth.vitalpath.services
import wslite.http.auth.HTTPBasicAuthorization
import wslite.rest.RESTClient
abstract class BaseService {
    protected static RESTClient restClient
    private String serviceBaseUrl
    public BaseService(serviceBaseURL, restClientAuthBaseAuthId, restClientAuthBaseAuthPwd){
        this.serviceBaseUrl = serviceBaseURL
        if (Objects.isNull(restClient)) {
            restClient = new RESTClient()
        }
        restClient.authorization = new HTTPBasicAuthorization(restClientAuthBaseAuthId, restClientAuthBaseAuthPwd)
    }
    def get(String path, sslTrustAllCerts = true) {
        restClient.setUrl(serviceBaseUrl)

        def response = restClient.get(path: path, sslTrustAllCerts: sslTrustAllCerts)
        assert response.statusCode == 200 : "Get REST call (${path}) failed"
        return response;
    }
    def getAll(String path, sslTrustAllCerts = true) {
        restClient.setUrl(serviceBaseUrl)
        def response = restClient.get(path: path, sslTrustAllCerts: sslTrustAllCerts)
        assert response.statusCode == 200 : "Get REST call (${path}) failed"
        return response;
    }
    def post(String path, Closure closure, sslTrustAllCerts = true ) {
        restClient.setUrl(serviceBaseUrl)
        def response = restClient.post(path: path, sslTrustAllCerts: sslTrustAllCerts, closure)
        assert response.statusCode == 200 : "Post REST call ((${serviceBaseUrl}/${path}) failed"
        return response
    }
    def put(String path, Closure closure, sslTrustAllCerts = true ) {
        restClient.setUrl(serviceBaseUrl)
        def response = restClient.put(path: path, sslTrustAllCerts: sslTrustAllCerts, closure)
        assert response.statusCode == 200 : "Put REST call ((${path}) failed"
        return response
    }
    def delete(String path, sslTrustAllCerts = true){
        restClient.setUrl(serviceBaseUrl)
        def response = restClient.delete(path: path, sslTrustAllCerts: sslTrustAllCerts)
        assert response.statusCode == 200 : "Delete REST call ((${path}) failed"
        return response
    }
}