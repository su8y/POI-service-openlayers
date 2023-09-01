class Pagination {
    total
    size
    number
    empty
    first
    last
    constructor(res) {
        this.total = res.totalPages
        this.size = res.size
        this.number = res.number
        this.empty = res.empty
        this.first = res.first
        this.last = res.last
    }
    makeHetosaPagination() {
        //5 단위로 끊어서.
        var list = []
        let targetPage = Math.floor(this.number / 5)


        for (var i = targetPage * 5; i < targetPage * 5 + 5; i++) {
            let madePageElement = this.#makePage(i, this.number === i)
            list.push(madePageElement);
        }
        return list
    }
    makePagination() {
        //5 단위로 끊어서.
        var list = []
        let targetPage = Math.floor(this.number / 5)

        list.push(this.#makeFirst())

        for (var i = targetPage * 5; i < targetPage * 5 + 5; i++) {
            let madePageElement = this.#makePage(i, this.number === i)
            list.push(madePageElement);

        }
        list.push(this.#makeLast())
        return list
    }

    #makePage(index, flag) {
        let li = document.createElement('li')
        li.classList.add('page-item');
        if (flag) li.classList.add('active');
        if (index >= this.total) li.classList.add('disabled')


        li.innerHTML = `
                <a href="?page=${index}" class="page-link">
                    <span aria-hidden="true">${index + 1}</span>
                </a>
            `

        return li

    }

    #makeFirst() {
        let li = document.createElement('li')
        var result;
        li.classList.add('page-item');
        if (this.first) li.classList.add('disabled')
        else {
            result = Math.floor(this.number / 5) * 5 - 1

        }

        li.innerHTML = `
                <a href="?page=${result}" class="page-link">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            `

        return li
    }

    #makeLast() {
        let li = document.createElement('li')
        li.classList.add('page-item');
        let firstElementNextPage = Math.floor(this.number / 5)
        firstElementNextPage = firstElementNextPage === 0 ? (firstElementNextPage + 1) * 5 : firstElementNextPage * 5
        if (firstElementNextPage >= this.total) li.classList.add('disabled')

        li.innerHTML = `
                <a class="page-link" href="?page=${firstElementNextPage}" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
          `

        return li
    }

}
export default Pagination;