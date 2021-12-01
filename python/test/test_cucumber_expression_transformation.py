import yaml
# TODO: generate a test method for every yaml file in the ../../testdata/cucumber-expresion/transformation directory
# TODO: create the class stub for CucumberExpression(expectation.expression, parameter_type_registry)
# TODO: create .github/workflows/test-python.yml

# inspired by the confest at https://docs.pytest.org/en/6.2.x/example/nonpython.html

def test_alternation():
    # Load the example from the yaml file in testdata
    expectation = yaml.safe_load('../../testdata/cucumber-expresion/transformation/alternation.yaml')

    parameter_type_registry = ParameterTypeRegistry()

    # create an instance of CucumberExpression
    expression = CucumberExpression(expectation.expression, parameter_type_registry)

    # Assert that the regexp source of the CucumberExpression matches the expected value from the yaml
    assert expression.regexp.source == expectation.expected_regex
