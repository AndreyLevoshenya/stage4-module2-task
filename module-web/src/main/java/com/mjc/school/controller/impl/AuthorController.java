package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.dto.PageDtoResponse;
import com.mjc.school.service.dto.SearchingRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mjc.school.controller.RestConstants.AUTHORS_V1_API_PATH;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = AUTHORS_V1_API_PATH)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, value = "Operations for creating, updating, retrieving and deleting authors in the application")
public class AuthorController implements BaseController<AuthorDtoRequest, AuthorDtoResponse, Long> {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    @ApiOperation(value = "View all authors", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all authors"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")})
    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("permitAll()")
    public ResponseEntity<PageDtoResponse<AuthorDtoResponse>> readAll(
            @RequestParam(name = "page", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "sort_by", required = false, defaultValue = "name:desc") String sortBy,
            @RequestParam(name = "search_by", required = false) String searchBy) {
        SearchingRequest searchingRequest = new SearchingRequest(pageNumber, pageSize, sortBy, searchBy);
        PageDtoResponse<AuthorDtoResponse> pageDtoResponse = authorService.readAll(searchingRequest);
        for (AuthorDtoResponse authorDtoResponse : pageDtoResponse.getEntityDtoList()) {
            Link selfRel = linkTo(AuthorController.class).slash(authorDtoResponse.getId()).withSelfRel();
            authorDtoResponse.add(selfRel);
        }

        return new ResponseEntity<>(pageDtoResponse, OK);
    }

    @Override
    @ApiOperation(value = "Get author by id", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved authors by id"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")})
    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthorDtoResponse> readById(@PathVariable Long id) {
        AuthorDtoResponse authorDtoResponse = authorService.readById(id);
        Link selfRel = linkTo(AuthorController.class).slash(id).withSelfRel();
        authorDtoResponse.add(selfRel);
        return new ResponseEntity<>(authorDtoResponse, OK);
    }

    @Override
    @ApiOperation(value = "Create author", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a author"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")})
    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<AuthorDtoResponse> create(@RequestBody AuthorDtoRequest createRequest) {
        AuthorDtoResponse authorDtoResponse = authorService.create(createRequest);
        Link selfRel = linkTo(AuthorController.class).slash(authorDtoResponse.getId()).withSelfRel();
        authorDtoResponse.add(selfRel);
        return new ResponseEntity<>(authorDtoResponse, CREATED);
    }

    @Override
    @ApiOperation(value = "Update author information", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated author information"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")})
    @PutMapping(value = "/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AuthorDtoResponse> update(@PathVariable Long id, @RequestBody AuthorDtoRequest updateRequest) {
        updateRequest.setId(id);
        AuthorDtoResponse authorDtoResponse = authorService.update(updateRequest);
        Link selfRel = linkTo(AuthorController.class).slash(id).withSelfRel();
        authorDtoResponse.add(selfRel);
        return new ResponseEntity<>(authorDtoResponse, OK);
    }

    @Override
    @ApiOperation(value = "Patch author information", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully patched author information"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")})
    @PatchMapping(value = "/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AuthorDtoResponse> patch(@PathVariable Long id, @RequestBody AuthorDtoRequest updateRequest) {
        updateRequest.setId(id);
        AuthorDtoResponse authorDtoResponse = authorService.patch(updateRequest);
        Link selfRel = linkTo(AuthorController.class).slash(id).withSelfRel();
        authorDtoResponse.add(selfRel);
        return new ResponseEntity<>(authorDtoResponse, OK);
    }

    @Override
    @ApiOperation(value = "Deletes specific author with the supplied id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deletes the specific author"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")})
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public void deleteById(@PathVariable Long id) {
        authorService.deleteById(id);
    }
}
