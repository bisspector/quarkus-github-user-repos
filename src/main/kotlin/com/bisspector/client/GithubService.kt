package com.bisspector.client

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.MultivaluedHashMap
import jakarta.ws.rs.core.MultivaluedMap
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import java.util.*


@RegisterRestClient(configKey = "repo-api")
//@ClientHeaderParam(name = "Authorization", value = ["Bearer \${github.token}"])
@RegisterClientHeaders(GithubTokenHeaderFactory::class)
interface GithubService {

    @GET
    @Path("/users/{username}/repos")
    suspend fun getReposByUsername(@PathParam("username") username: String): List<Repository>

    @GET
    @Path("/repos/{owner}/{repo}/branches")
    suspend fun getBranchesByRepoName(@PathParam("owner") owner: String, @PathParam("repo") repo: String): List<Branch>
}


@ApplicationScoped
class GithubTokenHeaderFactory : ClientHeadersFactory {
    @ConfigProperty(name = "github.token")
    lateinit var token: Optional<String>

    override fun update(incomingHeaders: MultivaluedMap<String, String>?, clientOutgoingHeaders: MultivaluedMap<String, String>?): MultivaluedMap<String, String> {
        val result: MultivaluedMap<String, String> = MultivaluedHashMap()
        if (token.isPresent) {
            result.add("Authorization", "Bearer ${token.get()}")
        }
        return result
    }

}

data class Repository(val name: String, val owner: RepositoryOwner, val fork: Boolean)
data class RepositoryOwner(val login: String)

data class Branch(val name: String, val commit: BranchCommit)
data class BranchCommit(val sha: String)