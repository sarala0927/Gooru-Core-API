package org.ednovo.gooru.domain.service;

import org.ednovo.gooru.core.api.model.ActionResponseDTO;
import org.ednovo.gooru.core.api.model.Country;
import org.ednovo.gooru.core.api.model.Province;
import org.ednovo.gooru.core.constant.ConstantProperties;
import org.ednovo.gooru.core.constant.ParameterProperties;
import org.ednovo.gooru.core.api.model.City;
import org.ednovo.gooru.domain.service.search.SearchResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

//import com.google.gdata.data.extensions.City;

@Service
public class CountryServiceImpl extends BaseServiceImpl implements CountryService, ParameterProperties, ConstantProperties {

	@Autowired
	private CountryRepository countryRepository;

	@Override
	public ActionResponseDTO<Country> createCountry(Country country) {
		final Errors errors = validateCountry(country);
		if (!errors.hasErrors()) {
			this.getCountryRepository().save(country);
		}
		return new ActionResponseDTO<Country>(country, errors);
	}

	@Override
	public Country updateCountry(String countryId, Country newCountry) {
		Country country = this.getCountryRepository().getCountry(countryId);
		rejectIfNull(country, GL0056, 404, "Country");
		if (newCountry.getName() != null) {
			country.setName(newCountry.getName());
		}
		this.getCountryRepository().save(country);
		return country;
	}

	@Override
	public Country getCountry(String countryId) {
		Country country = this.getCountryRepository().getCountry(countryId);
		rejectIfNull(country, GL0056, 404, "Country");
		return country;
	}

	@Override
	public SearchResults<Country> getCountries(Integer limit, Integer offset) {
		SearchResults<Country> result = new SearchResults<Country>();
		result.setSearchResults(this.getCountryRepository().getCountries(limit, offset));
		result.setTotalHitCount(this.getCountryRepository().getCountryCount());
		return result;
	}

	private Errors validateCountry(Country country) {
		final Errors errors = new BindException(country, "country");
		rejectIfNullOrEmpty(errors, country.getName(), NAME, GL0006, generateErrorMessage(GL0006, "Country name"));
		rejectIfNullOrEmpty(errors, country.getCountryId(), "countryId", GL0006, generateErrorMessage(GL0006, "Country"));
		return errors;
	}

	@Override
	public void deleteCountry(String countryId) {
		Country country = this.getCountryRepository().getCountry(countryId);
		rejectIfNull(country, GL0056, 404, "Country");
		this.getCountryRepository().remove(country);

	}

	public CountryRepository getCountryRepository() {
		return countryRepository;
	}

	@Override
	public ActionResponseDTO<Province> createState(Province province, String countryId) {
		final Errors errors = validateProvince(province);
		if (!errors.hasErrors()) {
			Country country = this.getCountryRepository().getCountry(countryId);
			rejectIfNull(country, GL0056, 404, "Province");
			province.setCountry(country);
			this.getCountryRepository().save(province);
		}

		return new ActionResponseDTO<Province>(province, errors);
	}

	@Override
	public Province updateState(String countryId, String stateId, Province newState) {
		Country country = this.getCountryRepository().getCountry(countryId);
		rejectIfNull(country, GL0056, 404, "Country");
		Province province = this.getCountryRepository().getState(countryId, stateId);
		rejectIfNull(province, GL0056, 404, "Province");
		if (newState.getName() != null) {
			province.setName(newState.getName());
		}
		this.getCountryRepository().save(province);
		return province;
	}

	@Override
	public Province getState(String countryId, String stateId) {
		Province province = this.getCountryRepository().getState(countryId, stateId);
		rejectIfNull(province, GL0056, 404, "Province");
		return province;
	}

	@Override
	public SearchResults<Province> getStates(String countryId, Integer limit, Integer offset) {
		SearchResults<Province> result = new SearchResults<Province>();
		result.setSearchResults(this.getCountryRepository().getStates(countryId, limit, offset));
		result.setTotalHitCount(this.getCountryRepository().getStateCount(countryId));
		return result;
	}

	@Override
	public void deleteState(String countryId, String stateId) {
		Province province = this.getCountryRepository().getState(countryId, stateId);
		rejectIfNull(province, GL0056, 404, "Province");
		this.getCountryRepository().remove(province);

	}

	private Errors validateProvince(Province province) {
		final Errors errors = new BindException(province, "Province");
		rejectIfNullOrEmpty(errors, province.getName(), NAME, GL0006, generateErrorMessage(GL0006, "State name"));
		rejectIfNullOrEmpty(errors, province.getStateId(), "stateId", GL0006, generateErrorMessage(GL0006, "Province"));
		return errors;
	}

	@Override
	public ActionResponseDTO<City> createCity(City city, String stateId, String countryId) {
		final Errors errors = validateCity(city);
		if (!errors.hasErrors()) {
			Country country = this.getCountryRepository().getCountry(countryId);
			Province province = this.getCountryRepository().getState(countryId, stateId);
			rejectIfNull(country, GL0056, 404, "Country");
			rejectIfNull(country, GL0056, 404, "Province");
			province.setCountry(country);
			city.setProvince(province);
			this.getCountryRepository().save(province);
			this.getCountryRepository().save(city);
		}
		return new ActionResponseDTO<City>(city, errors);
	}

	@Override
	public City updateCity(String stateId, String countryId, String cityId, City newCity) {
		Country country = this.getCountryRepository().getCountry(countryId);
		Province province = this.getCountryRepository().getState(countryId, stateId);
		rejectIfNull(country, GL0056, 404, "Country");
		rejectIfNull(province, GL0056, 404, "Province");
		City city = this.getCountryRepository().getCity(countryId, stateId, cityId);
		rejectIfNull(province, GL0056, 404, "Province");
		rejectIfNull(city, GL0056, 404, "City");
		if (newCity.getName() != null) {
			city.setName(newCity.getName());
		}
		this.getCountryRepository().save(city);
		return city;
	}

	@Override
	public City getCity(String countryId, String stateId, String cityId) {
		City city = this.getCountryRepository().getCity(countryId, stateId, cityId);
		rejectIfNull(city, GL0056, 404, "City");
		return city;
	}

	@Override
	public SearchResults<City> getCities(String countryId, String stateId, Integer limit, Integer offset) {
		SearchResults<City> result = new SearchResults<City>();
		result.setSearchResults(this.getCountryRepository().getCities(countryId, stateId, limit, offset));
		result.setTotalHitCount(this.getCountryRepository().getCityCount(countryId, stateId));
		return result;
	}

	@Override
	public void deleteCity(String countryId, String stateId, String cityId) {
		City city = this.getCountryRepository().getCity(countryId, stateId, cityId);
		rejectIfNull(city, GL0056, 404, "City");
		this.getCountryRepository().remove(city);
	}

	private Errors validateCity(City city) {
		final Errors errors = new BindException(city, "City");
		rejectIfNullOrEmpty(errors, city.getName(), NAME, GL0006, generateErrorMessage(GL0006, "City name"));
		rejectIfNullOrEmpty(errors, city.getCityId(), "stateId", GL0006, generateErrorMessage(GL0006, "Province"));
		return errors;
	}
}
