$(document).ready(function () {
    /********************************************************
     * comments
     */
    $(function () {
        $(".btn-comment").on("click", function (e) {
            e.preventDefault();
            let postBlock = $(this).closest(".post");
            let card = $('.card', postBlock);
            card.toggleClass('d-none');

        });

        $('.card-comment textarea').keydown(function (e) {
            if (e.keyCode == 13) {
                let card = $(this).closest('.card-comment');
                $('.btn-comment-save', card).trigger('click');
            }
        });

        $('.btn-comment-save').on('click', function (e) {
            e.preventDefault();

            let cardHeader = $(this).closest(".card-header");
            let cardBody = cardHeader.next('.card-body');
            let text = $('textarea', cardHeader).val();
            if (text.length == 0) {
                return;
            }
            let formData = new FormData();
            formData.append('post_id', $(this).data('post-id'));
            formData.append('comment', text);

            $.ajax({
                method: 'POST',
                processData: false,
                contentType: false,
                type: 'POST',
                url: '/comment/save',
                data: formData,

                success: function (response) {

                    let li = $('<li>');
                    li.addClass('list-group-item');
                    let small = $('<small>');
                    small.addClass('opacity-75').html(response[1]);
                    let pSmall = $('<small>');
                    pSmall.append(small);
                    let i = $('<i>');
                    i.append(pSmall);
                    li.append(i);
                    li.append($('<br>'));

                    let span = $('<span>');
                    span.html(formData.get('comment'));
                    li.append(span);

                    $('ul', cardBody).prepend(li);

                    $('textarea', cardHeader).val('');
                },
                error: function (jqXHR) {

                    console.log('Error: ' + jqXHR.status);
                }
            });

        });

    });

    /**************************************************************
     *  like / dislike
     *************************************************************/

    $('body').on('click', '.btn-comment-reaction',reactionClickHandler);
});

function reactionClickHandler(e) {
    e.preventDefault();
    let btn = $(this),
        btnIcon = $('i', btn);
    let btnGroup = btn.closest('.btn-group'),
        commentId = btnGroup.data('comment-id'),
        reaction = btnIcon.hasClass('fa-thumbs-up') ? 'like' : 'dislike';

    let formData = new FormData();
    formData.append('comment_id', commentId);
    formData.append('reaction', reaction);

    $.ajax({
        method: 'POST',
        processData: false,
        contentType: false,
        type: 'POST',
        url: '/reaction/add',
        data: formData,

        success: function (response) {

            let iconLike =  $('.fa-thumbs-up', btnGroup),
                iconDislike =  $('.fa-thumbs-down', btnGroup);

            if ('like' == reaction){
                iconLike.removeClass('far').addClass('fas text-primary');
                iconDislike.removeClass('fas text-primary').addClass('far');
            }  else {
                iconDislike.removeClass('far').addClass('fas text-primary');
                iconLike.removeClass('fas text-primary').addClass('far');
            }

            $('.like-counter', btnGroup).html(response.likeCounter == 0 ? '' : response.likeCounter);
            $('.dislike-counter', btnGroup).html(response.dislikeCounter == 0 ? '' : response.dislikeCounter);


        },
        error: function (jqXHR) {

            console.log('Error: ' + jqXHR.status);
        }
    });
}


/**************************************************************
 *  image preview
 *************************************************************/


const chooseFile = document.getElementById("choose-file");
const imgPreview = document.getElementById("img-preview");

function getImgData() {
    const files = chooseFile.files[0];
    if (files) {
        const fileReader = new FileReader();
        fileReader.readAsDataURL(files);
        fileReader.addEventListener("load", function () {
            imgPreview.style.display = "block";
            imgPreview.innerHTML = '<img src="' + this.result + '" />';
        });
    }
}
chooseFile.addEventListener("change", function () {
    getImgData();
});