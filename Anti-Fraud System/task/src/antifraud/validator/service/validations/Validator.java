package antifraud.validator.service.validations;

public interface Validator<T> {

    void validate(T data);

}
