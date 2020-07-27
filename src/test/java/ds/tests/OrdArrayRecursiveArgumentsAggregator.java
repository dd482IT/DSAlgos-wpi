package ds.tests;

import ds.IArray;
import ds.OrdArrayRecursive;
import java.util.Optional;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

class OrdArrayRecursiveArgumentsAggregator implements ArgumentsAggregator {

  public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context)
      throws ArgumentsAggregationException {
    Integer size = Optional.ofNullable(accessor.get(0, Integer.class)).orElse(100);
    Boolean strict = Optional.ofNullable(accessor.get(1, Boolean.class)).orElse(false);
    Object[] args = accessor.toArray();
    IArray array = new OrdArrayRecursive(size, strict);
    for (int i = 2; i < args.length; i++) array.insert(accessor.get(i, Long.class));
    return array;
  }
}
