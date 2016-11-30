function alertFilename()
            {
                var file = document.getElementById('file');
                var filename = file.value;
                if (filename.substring(3,11) == 'fakepath')
                { filename = filename.substring(12);}
                dest.size = filename.length;
                dest.value = filename;
            }