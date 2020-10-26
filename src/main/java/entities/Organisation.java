package entities;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Organisation {

    private int id;

    @NonNull
    private String name;

    @NonNull  //NonNull нужен для RequiredArgsConstructor
    private long taxpayerID;

    @NonNull
    private String checkingAccount;
}
