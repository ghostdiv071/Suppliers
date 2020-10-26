package entities;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Nomenclature {

    private int id;

    @NonNull   //NonNull нужен для RequiredArgsConstructor
    private long internalCode;

    @NonNull
    private String name;

}
