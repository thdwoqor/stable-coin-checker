package org.example.stablecoinchecker.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.stablecoinchecker.domain.cryptopair.CryptoPair;
import org.example.stablecoinchecker.domain.cryptopair.CryptoPairRepository;
import org.example.stablecoinchecker.domain.cryptopair.CryptoPairValidator;
import org.example.stablecoinchecker.domain.cryptosymbol.CryptoSymbol;
import org.example.stablecoinchecker.domain.cryptosymbol.CryptoSymbolRepository;
import org.example.stablecoinchecker.infra.cex.CryptoExchange;
import org.example.stablecoinchecker.service.dto.CryptoPairRequest;
import org.example.stablecoinchecker.service.dto.CryptoPairResponse;
import org.example.stablecoinchecker.service.dto.CryptoSymbolResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CryptoPairService {

    private final CryptoPairRepository cryptoPairRepository;
    private final CryptoSymbolRepository cryptoSymbolRepository;
    private final CryptoPairValidator validator;

    @Transactional(readOnly = true)
    public Map<CryptoExchange, List<CryptoSymbolResponse>> getCategories() {
        List<CryptoPair> pairs = cryptoPairRepository.findAll();
        return pairs.stream()
                .collect(Collectors.groupingBy(
                        CryptoPair::getCryptoExchange,
                        Collectors.mapping(
                                cryptoPair -> CryptoSymbolResponse.of(cryptoPair.getCryptoSymbol()),
                                Collectors.toList()
                        )
                ));
    }

    @Transactional(readOnly = true)
    public List<CryptoPairResponse> findAll() {
        return cryptoPairRepository.findAll()
                .stream()
                .map(CryptoPairResponse::of)
                .toList();
    }

    public void save(final CryptoPairRequest request) {
        CryptoSymbol cryptoSymbol = cryptoSymbolRepository.getById(request.cryptoSymbolId());
        cryptoPairRepository.save(new CryptoPair(
                request.cryptoExchange(),
                cryptoSymbol,
                validator
        ));
    }

    public List<CryptoPair> findByCryptoExchange(final CryptoExchange cryptoExchange) {
        return cryptoPairRepository.findByCryptoExchange(cryptoExchange);
    }

    public void edit(final Long pairId, final CryptoPairRequest request) {
        CryptoPair cryptoPair = cryptoPairRepository.getById(pairId);
        CryptoSymbol cryptoSymbol = cryptoSymbolRepository.getById(request.cryptoSymbolId());

        cryptoPair.update(
                request.cryptoExchange(),
                cryptoSymbol,
                validator
        );
    }

    public void delete(final Long pairId) {
        cryptoPairRepository.deleteById(pairId);
    }
}
