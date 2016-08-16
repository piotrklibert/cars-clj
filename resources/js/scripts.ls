{take, filter, map, fold1} = require "prelude-ls"
$ = require \jquery
Vue = require "vue"
Vue.config.debug = true


callWith = (val, f) --> f val
contains = (part, str) --> str.indexOf(part) != -1


$ ->
    results <- $.getJSON "/api/"
    window.vm = new Vue(
        el: "body"
        data: {results: results}
        methods:
            fetch: (a) ->
                res <- $.getJSON "/api/fetch/", {url: a.url}
                Vue.set a, \details, res
    )

    for x in vm.results
        vm.fetch(x)

    window.results = results
