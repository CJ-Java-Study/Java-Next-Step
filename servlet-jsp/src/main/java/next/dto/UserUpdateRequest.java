package next.dto;

public record UserUpdateRequest(
     String password,
     String name,
     String email){
}
