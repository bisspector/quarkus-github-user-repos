package com.bisspector

import com.bisspector.client.GithubService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.eclipse.microprofile.rest.client.inject.RestClient

@Provider
class ErrorExceptionMapper : ExceptionMapper<Exception> {
    override fun toResponse(exception: Exception): Response {
        return if (exception is WebApplicationException) {
            var errorMessage = "error";
            if (exception.response.status == 404) {
                errorMessage = "not found"
            }
            if (exception.response.status == 406) {
                errorMessage = "not acceptable"
            }

            return Response.status(exception.response.status)
                    .entity(ErrorResponse(exception.response.status, errorMessage))
                    .type(MediaType.APPLICATION_JSON_TYPE).build()
        } else {
            // Proper error logging here
            Response.serverError().entity("Internal Server Error").build()
        }
    }
}

@Path("/user")
class RepositoryResource(
        @RestClient private val service: GithubService
) {
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun greeting(@PathParam("username") username: String): Response {
        return Response.ok(service.getReposByUsername(username).filter { !it.fork }.map { repository ->
            RepositoryResponse(repository.name, repository.owner.login, service.getBranchesByRepoName(repository.owner.login, repository.name)
                    .map { branch -> BranchResponse(branch.name, branch.commit.sha) })
        }.toList()).build();
    }
}

data class ErrorResponse(val status: Int, val message: String)

data class RepositoryResponse(val name: String, val ownerLogin: String, val branches: List<BranchResponse>)
data class BranchResponse(val name: String, val sha: String)