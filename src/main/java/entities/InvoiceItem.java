package entities;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InvoiceItem {

    private int id;

    @NonNull  //NonNull нужен для RequiredArgsConstructor
    private long price;

    @NonNull
    private int amount;

    @NonNull
    private int nomenclature;

    @NonNull
    private int invoice_ID;

}
