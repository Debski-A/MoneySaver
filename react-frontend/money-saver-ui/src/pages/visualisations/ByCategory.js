import React, { Component } from "react";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Container from "react-bootstrap/Container";
import { withTranslation } from "react-i18next";
import i18n from "../../helpers/i18n";
import { VictoryChart, VictoryBar, VictoryTheme, VictoryAxis } from "victory";
import Dropdown from "react-bootstrap/Dropdown";
import Button from "react-bootstrap/Button";
import DatePicker from "react-datepicker";
import { getAccessToken } from "../../helpers/authenticationUtils";

class ByCategory extends Component {
  state = {
    typeDescription: "empty_type",
    categoryDescription: this.props.t("empty_category"),
    monthDescription: this.props.t("empty_month"),
    type: null,
    incomeCategories: this.props.incomeCategories,
    outcomeCategories: this.props.outcomeCategories,
    category: null,
    month: null,
    categories: null,
    currentLanguage: i18n.language,
    isDataLoaded: false,
    currency: "PLN",
    date: new Date(),
    data: [],
  };

  handleTypeOnClick = (enumVal, typeDescription) => {
    let categories;
    if (enumVal === 0) {
      categories = this.props.incomeCategories;
    } else {
      categories = this.props.outcomeCategories;
    }
    this.setState({
      typeDescription: typeDescription,
      type: enumVal,
      categories: categories,
    });
  };

  handleCategoryOnClick = (enumVal, categoryDescription) => {
    this.setState({
      categoryDescription: categoryDescription,
      category: enumVal,
    });
  };

  handleDateChange = (newDate) => {
    this.setState({
      date: newDate,
    });
  };

  handleCurrencyChange = (currency) => {
    this.setState({
      currency: currency,
    });
  };

  getData = () => {
    let incomeUpdateURL = "http://localhost/api/calculations/calculate";
    let currentLanguage = i18n.language;
    let incomeCategory = null;
    let outcomeCategory = null;
    if (this.state.type === 0) {
      incomeCategory = this.state.category > 3 ? 3 : this.state.category;
    } else {
      outcomeCategory = this.state.category;
    }
    let calculationData = {
      calculationType: this.state.type,
      incomeCategory: incomeCategory, // incomeCategory i outcomeCategory takie samo, bo type decyduje co bedzie kalkulowane
      outcomeCategory: outcomeCategory,
      currency: this.state.currency,
      startDate: this.state.date,
    };
    fetch(incomeUpdateURL, {
      method: "POST",
      body: JSON.stringify(calculationData),
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + getAccessToken(),
        "Accept-Language": currentLanguage,
      },
    })
      .then((response) => response.json())
      .then((response) => {
        if (!!response.errorMessage) {
          console.log(response.errorMessage);
          this.setState({
            data: [],
          });
        } else if (!!response.status && response.status !== 200) {
          throw new Error("Status not OK");
        } else {
          let rawData = response.visualisationPoints;
          let responseData = rawData.map((i) => {
            return { x: new Date(i.date).getDate(), y: i.value };
          });
          console.log(responseData);
          this.setState({
            data: responseData,
          });
        }
      })
      .catch((err) => {
        console.log(err);
        this.setState({
          data: [],
        });
      });
  };

  render() {
    return (
      <Container
        fluid
        style={{ flexWrap: "wrap" }}
        className="d-flex fill-height"
      >
        <Row className="d-flex fill-height">
          <VictoryChart
            width={1500}
            height={600}
            theme={VictoryTheme.material}
            domainPadding={{ x: 10 }}
          >
            <VictoryAxis
              tickCount={20}
              crossAxis={false}
              domain={[0, 1000]}
              dependentAxis
            />
            <VictoryAxis tickCount={31} />
            <VictoryBar
              // domain={[1, 31]}
              labels={({ datum }) => datum.y}
              style={{
                data: {
                  fill: "#c43a31",
                  width: 15,
                },
              }}
              data={this.state.data}
            />
          </VictoryChart>
        </Row>
        <Row className="d-flex fill-height">
          <Col sm={4} lg={4} xs={4} md={4}>
            <Row>
              <Col>
                <Dropdown drop="up">
                  <Dropdown.Toggle
                    variant="secondary"
                    className="w-100"
                    size="lg"
                  >
                    {this.props.t("type")}
                  </Dropdown.Toggle>
                  <Dropdown.Menu>
                    <Dropdown.Item
                      onClick={() => this.handleTypeOnClick(0, "income")}
                    >
                      {this.props.t("income")}
                    </Dropdown.Item>
                    <Dropdown.Item
                      onClick={() => this.handleTypeOnClick(1, "outcome")}
                    >
                      {this.props.t("outcome")}
                    </Dropdown.Item>
                  </Dropdown.Menu>
                </Dropdown>
              </Col>
              <Col>
                <label className="centered-label">
                  {this.props.t(this.state.typeDescription)}
                </label>
              </Col>
            </Row>
          </Col>
          <Col sm={4} lg={4} xs={4} md={4}>
            {this.state.type !== null ? (
              <Row>
                <Col>
                  <Dropdown drop="up">
                    <Dropdown.Toggle
                      variant="secondary"
                      className="w-100"
                      size="lg"
                    >
                      {this.props.t("category")}
                    </Dropdown.Toggle>
                    <Dropdown.Menu>
                      {Object.keys(this.state.categories).map((key) => (
                        <Dropdown.Item
                          onClick={() =>
                            this.handleCategoryOnClick(
                              key,
                              this.state.categories[key]
                            )
                          }
                          key={key}
                        >
                          {this.state.categories[key]}
                        </Dropdown.Item>
                      ))}
                    </Dropdown.Menu>
                  </Dropdown>
                </Col>
                <Col>
                  <label className="centered-label">
                    {this.state.categories[this.state.category]}
                  </label>
                </Col>
              </Row>
            ) : null}
          </Col>{" "}
          <Col sm={4} lg={4} xs={4} md={4}>
            {this.state.category !== null ? (
              <>
                <Row>
                  <Col>
                    <Row>
                      <label className="centered-label">
                        {this.props.t("month")}
                      </label>
                    </Row>
                    <Row>
                      <DatePicker
                        locale={this.props.i18n.language}
                        selected={this.state.date}
                        onChange={this.handleDateChange}
                        dateFormat="MM/yyyy"
                        showMonthYearPicker
                      />
                    </Row>
                  </Col>
                  <Col>
                    <Row>
                      <Dropdown drop="up">
                        <Dropdown.Toggle
                          variant="secondary"
                          className="w-100"
                          size="sm"
                        >
                          {this.state.currency}
                        </Dropdown.Toggle>
                        <Dropdown.Menu>
                          {Object.keys(this.props.currencies).map((key) => (
                            <Dropdown.Item
                              key={key}
                              onClick={() =>
                                this.handleCurrencyChange(
                                  this.props.currencies[key]
                                )
                              }
                            >
                              {this.props.currencies[key]}
                            </Dropdown.Item>
                          ))}
                        </Dropdown.Menu>
                      </Dropdown>
                    </Row>
                    <Row>
                      <Button
                        variant="secondary"
                        size="sm"
                        className="w-50 p-2"
                        title="GO!"
                        onClick={this.getData}
                      >
                        GO!
                      </Button>
                    </Row>
                  </Col>
                </Row>
              </>
            ) : null}
          </Col>
        </Row>
      </Container>
    );
  }

  componentDidUpdate() {
    if (
      this.state.currentLanguage !== i18n.language ||
      this.state.incomeCategories !== this.props.incomeCategories ||
      this.state.outcomeCategories !== this.props.outcomeCategories
    ) {
      if (this.state.type === 0) {
        this.setState({
          currentLanguage: i18n.language,
          categories: this.props.incomeCategories,
          incomeCategories: this.props.incomeCategories,
          outcomeCategories: this.props.outcomeCategories,
        });
      } else {
        this.setState({
          currentLanguage: i18n.language,
          categories: this.props.outcomeCategories,
          incomeCategories: this.props.incomeCategories,
          outcomeCategories: this.props.outcomeCategories,
        });
      }
    }
  }
}

export default withTranslation("by_category")(ByCategory);
